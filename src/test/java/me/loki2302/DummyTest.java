package me.loki2302;

import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void dummy() throws IOException {
        DummyMessage dummyMessage = makeTestMyMessage();

        MessagePack messagePack = new MessagePack();
        byte[] data = messagePack.write(dummyMessage);
        DummyMessage readMessage = messagePack.read(data, DummyMessage.class);

        assertMyMessageEquals(dummyMessage, readMessage);
    }

    private static DummyMessage makeTestMyMessage() {
        DummyMessage dummyMessage = new DummyMessage();
        dummyMessage.name = "hello there";
        dummyMessage.version = 3.14;
        return dummyMessage;
    }

    private static void assertMyMessageEquals(DummyMessage expected, DummyMessage actual) {
        assertEquals(expected.name, actual.name);
        assertEquals(expected.version, actual.version, 0.001);
    }

    @Message
    public static class DummyMessage {
        public String name;
        public double version;

        @Override
        public String toString() {
            return String.format("Message{%s,%f}", name, version);
        }
    }
}
