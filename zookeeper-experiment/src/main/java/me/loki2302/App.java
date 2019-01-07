package me.loki2302;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zkMain = new ZooKeeper("127.0.0.1:2181", 5000, null);
        try {
            zkMain.create("/omg", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            Thread.sleep(5000);
        } finally {
            zkMain.close();
        }
    }
}
