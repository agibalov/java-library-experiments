package me.loki2302;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TransactionsTest {
    private final static String JDBC_URL = "jdbc:hsqldb:mem:test";
    private final static String JDBC_USER = "sa";
    private final static String JDBC_PASSWORD = "";

    private ExecutorService executorServiceA;
    private Actor actorA;

    private ExecutorService executorServiceB;
    private Actor actorB;

    @Before
    public void makeActors() throws ClassNotFoundException, SQLException {
        executorServiceA = Executors.newSingleThreadExecutor();
        SyncExecutor syncExecutorA = new SyncExecutor(executorServiceA);
        actorA = new Actor(syncExecutorA);

        executorServiceB = Executors.newSingleThreadExecutor();
        SyncExecutor syncExecutorB = new SyncExecutor(executorServiceB);
        actorB = new Actor(syncExecutorB);

        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement s = connection.prepareStatement(
                    "create table Notes(text varchar(256) not null)")) {
                s.executeUpdate();
            }

            boolean supportsSerializable = connection.getMetaData()
                    .supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
            assertTrue(supportsSerializable);
        }
    }

    @After
    public void killAuthors() throws Exception {
        actorA.closeConnection();
        actorA = null;

        actorB.closeConnection();
        actorB = null;

        executorServiceA.shutdown();
        executorServiceA = null;

        executorServiceB.shutdown();
        executorServiceB = null;
    }

    @Test
    public void serializableIsolationLevelWorks() throws Exception {
        actorA.openConnection();
        actorB.openConnection();

        assertEquals(0, actorA.selectAllRows());
        assertEquals(0, actorB.selectAllRows());

        // this throws "statement execution aborted: timeout reached" in HSQLDB 2.3.1
        actorA.insertRow("actor A");
        assertEquals("actorA is expected to see the change it has just made", 1, actorA.selectAllRows());

        // here, actorB sees the changes that actorA has made (in HSQLDB 2.3.2)
        assertEquals("actorB is expected to see no changes made by actorA", 0, actorB.selectAllRows());

        try {
            actorB.insertRow("actor B");
            fail("actorB managed to update the table after actorA has already done it");
        } catch (SQLTransactionRollbackException e) {
            // intentionally left blank
        } finally {
            actorA.commit();
            actorB.commit();
        }

        int noteCount = 0;
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try(PreparedStatement s = connection.prepareStatement("select * from Notes")) {
                try(ResultSet resultSet = s.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.printf("xxx %s\n", resultSet.getString(1));
                        ++noteCount;
                    }
                }
            }
        }

        assertEquals(1, noteCount);
    }

    public static class Actor {
        private final SyncExecutor syncExecutor;
        private Connection connection;

        public Actor(SyncExecutor syncExecutor) {
            this.syncExecutor = syncExecutor;
        }

        public void openConnection() throws Exception {
            connection = syncExecutor.execute(() -> {
                Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                connection.setAutoCommit(false);
                connection.setReadOnly(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                return connection;
            });
        }

        public int selectAllRows() throws Exception {
            return syncExecutor.execute(() -> {
                int rowCount = 0;
                try (PreparedStatement s = connection.prepareStatement("select * from Notes")) {
                    s.setQueryTimeout(1);
                    try (ResultSet resultSet = s.executeQuery()) {
                        while (resultSet.next()) {
                            ++rowCount;
                        }
                    }
                }

                return rowCount;
            });
        }

        public void insertRow(String text) throws Exception {
            syncExecutor.execute(() -> {
                try (PreparedStatement s = connection.prepareStatement("insert into Notes(text) values(?)")) {
                    s.setString(1, text);
                    s.setQueryTimeout(1);
                    s.executeUpdate();
                }
            });
        }

        public void commit() throws Exception {
            syncExecutor.execute(() -> connection.commit());
        }

        public void closeConnection() throws Exception {
            syncExecutor.execute(() -> connection.close());
            connection = null;
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
