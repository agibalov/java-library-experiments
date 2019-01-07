package me.loki2302;

import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ZooKeeperTest {
    private TestingServer zookeeperServer;

    @Before
    public void startup() throws Exception {
        zookeeperServer = new TestingServer(2181);
        zookeeperServer.start();
    }

    @After
    public void shutdown() throws IOException {
        zookeeperServer.close();
    }

    @Test
    public void dummy() throws KeeperException, InterruptedException, IOException {
        assertOmgExists(false);

        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 5000, null);
        try {
            zk.create("/omg", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            assertOmgExists(true);
        } finally {
            zk.close();
        }

        assertOmgExists(false);
    }

    private static void assertOmgExists(boolean shouldExist) throws KeeperException, InterruptedException, IOException {
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 5000, null);
        try {
            Stat stat = zk.exists("/omg", false);
            if(shouldExist) {
                assertNotNull(stat);
            } else {
                assertNull(stat);
            }
        } finally {
            zk.close();
        }
    }
}
