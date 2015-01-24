package me.loki2302;

import org.junit.Test;

import java.sql.*;
import java.util.concurrent.Exchanger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransactionsTest {
    private final static String JDBC_URL = "jdbc:hsqldb:mem:test";
    private final static String JDBC_USER = "sa";
    private final static String JDBC_PASSWORD = "";

    @Test
    public void dummy() throws ClassNotFoundException, SQLException, InterruptedException {
        Class.forName("org.hsqldb.jdbcDriver");

        try(Connection connection = makeConnection()) {
            try (PreparedStatement s = connection.prepareStatement(
                    "create table Notes(id int not null, text varchar(256) not null)")) {
                s.executeUpdate();
            }

            boolean supportsSerializable = connection.getMetaData()
                    .supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
            System.out.printf("supports serializable: %b\n", supportsSerializable);
        }

        final Exchanger<Void> thread1BeforeInsertExchanger = new Exchanger<>();
        final Exchanger<Void> thread1BeforeCommitExchanger = new Exchanger<>();
        final Actor actor1 = new Actor("thread1", thread1BeforeInsertExchanger, thread1BeforeCommitExchanger);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    actor1.act();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        final Exchanger<Void> thread2BeforeInsertExchanger = new Exchanger<>();
        final Exchanger<Void> thread2BeforeCommitExchanger = new Exchanger<>();
        final Actor actor2 = new Actor("thread2", thread2BeforeInsertExchanger, thread2BeforeCommitExchanger);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    actor2.act();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        thread1BeforeInsertExchanger.exchange(null);
        thread2BeforeInsertExchanger.exchange(null);
        thread2BeforeCommitExchanger.exchange(null);
        thread1BeforeCommitExchanger.exchange(null);

        try(Connection connection = makeConnection()) {
            try(PreparedStatement s = connection.prepareStatement("select * from Notes")) {
                try(ResultSet resultSet = s.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.printf("xxx %d %s\n", resultSet.getInt(1), resultSet.getString(2));
                    }
                }
            }
        }

        assertTrue(actor1.managedToInsert());
        assertFalse(actor2.managedToInsert());
    }

    private static Connection makeConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    private static class Actor {
        private final String name;
        private final Exchanger<Void> beforeInsertExchanger;
        private final Exchanger<Void> beforeCommitExchanger;
        private boolean managedToInsert;

        public Actor(
                String name,
                Exchanger<Void> beforeInsertExchanger,
                Exchanger<Void> beforeCommitExchanger) {

            this.name = name;
            this.beforeInsertExchanger = beforeInsertExchanger;
            this.beforeCommitExchanger = beforeCommitExchanger;
        }

        public boolean managedToInsert() {
            return managedToInsert;
        }

        public void act() throws SQLException, InterruptedException {
            try(Connection connection = makeConnection()) {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                try (PreparedStatement s = connection.prepareStatement("select * from Notes")) {
                    s.setQueryTimeout(1);
                    try (ResultSet resultSet = s.executeQuery()) {
                        while (resultSet.next()) {
                            System.out.printf("%s: %d %s\n", name, resultSet.getInt(1), resultSet.getString(2));
                        }
                    }
                }

                try (PreparedStatement s = connection.prepareStatement("insert into Notes(id, text) values(?, ?)")) {
                    s.setInt(1, 1);
                    s.setString(2, "created by " + name);
                    s.setQueryTimeout(1);

                    beforeInsertExchanger.exchange(null);

                    managedToInsert = false;
                    try {
                        s.executeUpdate();
                        managedToInsert = true;
                    } catch(Exception e) {
                        managedToInsert = false;
                    }
                }

                beforeCommitExchanger.exchange(null);

                connection.commit();

                System.out.printf("%s succeeded\n", name);
            }
        }
    }
}
