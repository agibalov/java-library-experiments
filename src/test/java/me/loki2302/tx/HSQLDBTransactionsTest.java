package me.loki2302.tx;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HSQLDBTransactionsTest {
    private final static String JDBC_URL = "jdbc:hsqldb:mem:test;hsqldb.tx=mvcc";
    private final static String JDBC_USER = "sa";
    private final static String JDBC_PASSWORD = "";

    @Before
    public void setUpDatabase() throws SQLException {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement s = connection.prepareStatement("create table Notes(text varchar(256) not null)")) {
                s.executeUpdate();
            }
        }
    }

    @After
    public void shutdownDatabase() throws SQLException {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement s = connection.prepareStatement("shutdown")) {
                s.execute();
            }
        }
    }

    @Test
    public void whenIsolationIsReadUncommittedThenUncommittedChangesAreVisible() throws SQLException {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try(PreparedStatement s = connection.prepareStatement("insert into Notes(text) values(?)")) {
                s.setString(1, "original");
                s.executeUpdate();
            }
        }

        try(Connection connection1 = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            connection1.setAutoCommit(false);

            try(Connection connection2 = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                connection2.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                connection2.setAutoCommit(false);

                assertEquals("original", selectOnly(connection1));
                assertEquals("original", selectOnly(connection2));

                updateOnly(connection1, "updated");

                assertEquals("updated", selectOnly(connection1));

                // connection2 may see "updated", but it is not guaranteed:
                // both "updated" and "original" are the valid values
                String asSeenByConnection2 = selectOnly(connection2);
                assertTrue(asSeenByConnection2.equals("original") || asSeenByConnection2.equals("updated"));

                connection1.commit();

                assertEquals("updated", selectOnly(connection1));

                // connection2 may see "updated", but it is not guaranteed:
                // both "updated" and "original" are the valid values
                asSeenByConnection2 = selectOnly(connection2);
                assertTrue(asSeenByConnection2.equals("original") || asSeenByConnection2.equals("updated"));
            }
        }
    }

    @Test
    public void whenIsolationIsReadCommittedThenUncommittedChangesAreNotVisible() throws SQLException {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try(PreparedStatement s = connection.prepareStatement("insert into Notes(text) values(?)")) {
                s.setString(1, "original");
                s.executeUpdate();
            }
        }

        try(Connection connection1 = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            connection1.setAutoCommit(false);

            try(Connection connection2 = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                connection2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection2.setAutoCommit(false);

                assertEquals("original", selectOnly(connection1));
                assertEquals("original", selectOnly(connection2));

                updateOnly(connection1, "updated");

                assertEquals("updated", selectOnly(connection1));
                // connection2 can only see "original", because the change
                // by connection1 is not committed yet
                assertEquals("original", selectOnly(connection2));

                connection1.commit();

                assertEquals("updated", selectOnly(connection1));

                // connection2 must see "updated", because this value is already committed
                assertEquals("updated", selectOnly(connection2));
            }
        }
    }

    @Test
    public void whenIsolationIsRepeatableReadThenNeitherUncommittedNorCommittedChangesAreVisible() throws SQLException {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try(PreparedStatement s = connection.prepareStatement("insert into Notes(text) values(?)")) {
                s.setString(1, "original");
                s.executeUpdate();
            }
        }

        try(Connection connection1 = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            connection1.setAutoCommit(false);

            try(Connection connection2 = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                connection2.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                connection2.setAutoCommit(false);

                assertEquals("original", selectOnly(connection1));
                assertEquals("original", selectOnly(connection2));

                updateOnly(connection1, "updated");

                assertEquals("updated", selectOnly(connection1));
                // the first read was "original", so all the next reads should be "original" as well
                assertEquals("original", selectOnly(connection2));

                connection1.commit();

                assertEquals("updated", selectOnly(connection1));
                // the first read was "original", so all the next reads should be "original" as well
                assertEquals("original", selectOnly(connection2));
            }
        }
    }

    @Test
    public void whenIsolationIsSerializableThenTableLevelUpdatesAreNotVisible() throws SQLException {
        try(Connection connection1 = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            connection1.setAutoCommit(false);

            try(Connection connection2 = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                connection2.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection2.setAutoCommit(false);

                assertEquals(0, selectAll(connection1));
                assertEquals(0, selectAll(connection2));

                insertOne(connection1);

                assertEquals(1, selectAll(connection1));
                assertEquals(0, selectAll(connection2));

                connection1.commit();

                assertEquals(1, selectAll(connection1));
                assertEquals(0, selectAll(connection2));
            }
        }
    }

    // behaves the same way the non-Threads version does
    @Test
    public void Threads_whenIsolationIsSerializableThenTableLevelUpdatesAreNotVisible() throws Exception {
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        SyncExecutor syncExecutor1 = new SyncExecutor(executorService1);
        try {
            ExecutorService executorService2 = Executors.newSingleThreadExecutor();
            SyncExecutor syncExecutor2 = new SyncExecutor(executorService2);
            try {
                Connection connection1 = syncExecutor1.execute(() -> {
                    Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                    connection.setAutoCommit(false);
                    return connection;
                });
                try {
                    Connection connection2 = syncExecutor2.execute(() -> {
                        Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                        connection.setAutoCommit(false);
                        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                        return connection;
                    });
                    try {

                        assertEquals(0, (int) syncExecutor1.execute(() -> selectAll(connection1)));
                        assertEquals(0, (int) syncExecutor2.execute(() -> selectAll(connection2)));

                        syncExecutor1.execute(() -> insertOne(connection1));

                        assertEquals(1, (int) syncExecutor1.execute(() -> selectAll(connection1)));
                        assertEquals(0, (int) syncExecutor2.execute(() -> selectAll(connection2)));

                        syncExecutor1.execute(() -> connection1.commit());

                        assertEquals(1, (int) syncExecutor1.execute(() -> selectAll(connection1)));
                        assertEquals(0, (int) syncExecutor2.execute(() -> selectAll(connection2)));
                    } finally {
                        syncExecutor2.execute(() -> connection2.close());
                    }
                } finally {
                    syncExecutor1.execute(() -> connection1.close());
                }
            } finally {
                executorService2.shutdown();
            }
        } finally {
            executorService1.shutdown();
        }
    }

    private static int selectAll(Connection connection) throws SQLException {
        int count = 0;
        try (PreparedStatement s = connection.prepareStatement("select * from Notes")) {
            s.setQueryTimeout(1);
            try (ResultSet resultSet = s.executeQuery()) {
                while (resultSet.next()) {
                    ++count;
                }
            }
        }

        return count;
    }

    private static String selectOnly(Connection connection) throws SQLException {
        try (PreparedStatement s = connection.prepareStatement("select * from Notes")) {
            s.setQueryTimeout(1);
            try (ResultSet resultSet = s.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getString(1);
                }
            }
        }

        throw new RuntimeException();
    }

    private static void insertOne(Connection connection) throws SQLException {
        try (PreparedStatement s = connection.prepareStatement("insert into Notes(text) values(?)")) {
            s.setString(1, "hello");
            s.setQueryTimeout(1);
            s.executeUpdate();
        }
    }

    private static void updateOnly(Connection connection, String text) throws SQLException {
        try(PreparedStatement s = connection.prepareStatement("update Notes set text = ?")) {
            s.setString(1, text);
            s.setQueryTimeout(1);
            s.executeUpdate();
        }
    }

    public static class SyncExecutor {
        private final ExecutorService executorService;

        public SyncExecutor(ExecutorService executorService) {
            this.executorService = executorService;
        }

        public void execute(Throwing throwing) throws Exception {
            execute(() -> {
                throwing.run();
                return null;
            });
        }

        public <TResult> TResult execute(Callable<TResult> callable) throws Exception {
            Future<TResult> resultFuture = executorService.submit(callable);
            try {
                return resultFuture.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw (Exception)e.getCause();
            }
        }
    }

    public static interface Throwing {
        void run() throws Exception;
    }
}
