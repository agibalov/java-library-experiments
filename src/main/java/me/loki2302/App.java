package me.loki2302;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class App {
    public static void main(String[] args) {
        Config config = new Config();
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

        try {
            Map<Integer, String> customers = hazelcastInstance.getMap("users");
            customers.put(1, "loki2302");
            customers.put(2, "qwerty");

            System.out.println(customers.get(1));
        } finally {
            // also: do not shutdown and run the second instance of this app,
            // they'll make a cluster
            hazelcastInstance.shutdown();
        }
    }
}
