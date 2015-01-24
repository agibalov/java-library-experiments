package me.loki2302;

import org.junit.Test;

import java.sql.*;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TransactionsTest {
    private final static String JDBC_URL = "jdbc:hsqldb:mem:test";
    private final static String JDBC_USER = "sa";
    private final static String JDBC_PASSWORD = "";

    @Test
    public void dummy() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");

        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement s = connection.prepareStatement(
                    "create table Notes(id int not null, text varchar(256) not null)")) {
                s.executeUpdate();
            }

            boolean supportsSerializable = connection.getMetaData()
                    .supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
            System.out.printf("supports serializable: %b\n", supportsSerializable);
        }

        ExecutorService executorServiceA = Executors.newSingleThreadExecutor();
        SyncExecutor syncExecutorA = new SyncExecutor(executorServiceA);

        ExecutorService executorServiceB = Executors.newSingleThreadExecutor();
        SyncExecutor syncExecutorB = new SyncExecutor(executorServiceB);

        Connection connectionA = syncExecutorA.execute(() -> {
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return connection;
        });
        Connection connectionB = syncExecutorB.execute(() -> {
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return connection;
        });

        syncExecutorA.execute(() -> {
            try (PreparedStatement s = connectionA.prepareStatement("select * from Notes")) {
                s.setQueryTimeout(1);
                try (ResultSet resultSet = s.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.printf("A: %d %s\n", resultSet.getInt(1), resultSet.getString(2));
                    }
                }
            }
        });
        syncExecutorB.execute(() -> {
            try (PreparedStatement s = connectionB.prepareStatement("select * from Notes")) {
                s.setQueryTimeout(1);
                try (ResultSet resultSet = s.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.printf("B: %d %s\n", resultSet.getInt(1), resultSet.getString(2));
                    }
                }
            }
        });

        syncExecutorA.execute(() -> {
            try (PreparedStatement s = connectionA.prepareStatement("insert into Notes(id, text) values(?, ?)")) {
                s.setInt(1, 1);
                s.setString(2, "created by A");
                s.setQueryTimeout(1);
                s.executeUpdate();
            }
        });
        try {
            syncExecutorB.execute(() -> {
                try (PreparedStatement s = connectionB.prepareStatement("insert into Notes(id, text) values(?, ?)")) {
                    s.setInt(1, 1);
                    s.setString(2, "created by B");
                    s.setQueryTimeout(1);
                    s.executeUpdate();
                }
            });

            fail("client 2 managed to update the table after client 1 has already done it");
        } catch(SQLTransactionRollbackException e) {
            System.out.println("hurray!");
        } finally {
            syncExecutorA.execute(() -> connectionA.commit());
            syncExecutorB.execute(() -> connectionB.commit());

            syncExecutorA.execute(() -> connectionA.close());
            syncExecutorB.execute(() -> connectionB.close());

            executorServiceA.shutdown();
            executorServiceB.shutdown();

            int noteCount = 0;
            try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                try(PreparedStatement s = connection.prepareStatement("select * from Notes")) {
                    try(ResultSet resultSet = s.executeQuery()) {
                        while (resultSet.next()) {
                            System.out.printf("xxx %d %s\n", resultSet.getInt(1), resultSet.getString(2));
                            ++noteCount;
                        }
                    }
                }
            }

            assertEquals(1, noteCount);
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
