package me.loki2302;

import kafka.admin.AdminUtils;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws InterruptedException, ExecutionException, BrokenBarrierException {
        kafkaProducerConsumerHelloWorld();
    }

    private static void dummyProducer() throws ExecutionException, InterruptedException {
        String topicName = "the-topic";

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("client.id", "DemoProducer");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        Random r = new Random();
        while(true) {
            for (int i = 0; i < 10; ++i) {
                String key = String.format("key-%d", i + 1);
                String value = String.valueOf(r.nextInt(1000));
                producer.send(new ProducerRecord<String, String>(topicName, key, value)).get();
                Thread.sleep(5);
            }
            Thread.sleep(300);
        }
    }

    public static int kafkaProducerConsumerHelloWorld() throws InterruptedException, ExecutionException, BrokenBarrierException {
        // Topic removal functionality doesn't seem to work reliably, event with "delete.topic.enable" set to true,
        // so I have to create a new topic on each run
        String topicName = "topic-" + UUID.randomUUID().toString();

        // looks like this doesn't actually create a topic
        ZkClient zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
        AdminUtils.createTopic(zkClient, topicName, 1, 1, new Properties());
        zkClient.close();

        // here, it's important to have the topic created BEFORE consumer starts listening to it
        CyclicBarrier subscriptionBarrier = new CyclicBarrier(2);
        ConsumerThread consumerThread = new ConsumerThread(topicName, subscriptionBarrier);
        ProducerThread producerThread = new ProducerThread(1000, topicName);

        consumerThread.start();
        subscriptionBarrier.await();

        producerThread.start();
        producerThread.join();
        consumerThread.join();

        int numberOfMessagesConsumed = consumerThread.getNumberOfMessagesConsumed();
        System.out.println(numberOfMessagesConsumed);

        return numberOfMessagesConsumed;
    }

    public static class ConsumerThread extends Thread {
        private final String topicName;
        private final CyclicBarrier subscriptionBarrier;
        private volatile int numberOfMessagesConsumed;

        public ConsumerThread(String topicName, CyclicBarrier subscriptionBarrier) {
            this.topicName = topicName;
            this.subscriptionBarrier = subscriptionBarrier;
        }

        @Override
        public void run() {
            Properties properties = new Properties();
            properties.put("zookeeper.connect", "localhost:2181");
            properties.put("group.id", "group1");
            properties.put("zookeeper.session.timeout.ms", "400");
            properties.put("zookeeper.sync.time.ms", "200");
            properties.put("auto.commit.interval.ms", "1000");

            ConsumerConfig consumerConfig = new ConsumerConfig(properties);
            ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

            Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
            topicCountMap.put(topicName, 1);

            Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
            KafkaStream<byte[], byte[]> stream = consumerMap.get(topicName).get(0);

            try {
                System.out.println("LISTENING!");
                subscriptionBarrier.await();
                for (MessageAndMetadata<byte[], byte[]> messageAndMetadata : stream) {
                    String key = new String(messageAndMetadata.key());
                    String message = new String(messageAndMetadata.message());
                    System.out.printf("message: [%s] %s\n", key, message);
                    if(message.equals("bye")) {
                        System.out.println("Got the 'bye' message, going to stop comsumption");
                        break;
                    }

                    ++numberOfMessagesConsumed;
                }
            } catch (RuntimeException e) { // InterruptedException actually
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } finally {
                consumerConnector.shutdown();
            }
        }

        public int getNumberOfMessagesConsumed() {
            return numberOfMessagesConsumed;
        }
    }

    public static class ProducerThread extends Thread {
        private final int numberOfMessagesToProduce;
        private final String topicName;

        public ProducerThread(int numberOfMessagesToProduce, String topicName) {
            this.numberOfMessagesToProduce = numberOfMessagesToProduce;
            this.topicName = topicName;
        }

        @Override
        public void run() {
            Properties properties = new Properties();
            properties.put("bootstrap.servers", "localhost:9092");
            properties.put("client.id", "DemoProducer");
            properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
            try {
                for (int i = 0; i < numberOfMessagesToProduce; ++i) {
                    String id = String.format("message-%d", i);
                    String message = String.format("I am message %d", i);
                    try {
                        producer.send(new ProducerRecord<String, String>(topicName, id, message)).get();
                        System.out.printf("sent: [%s] %s\n", id, message);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    producer.send(new ProducerRecord<String, String>(topicName, "bye", "bye")).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } catch(InterruptedException e) {
                // intentionally blank
            } finally {
                producer.close();
            }
        }
    }
}
