package me.loki2302;

import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void canWriteAndReadAMessage() throws IOException {
        DummyMessage dummyMessage = makeDummyMessage(0);

        MessagePack messagePack = new MessagePack();
        byte[] data = messagePack.write(dummyMessage);
        DummyMessage readMessage = messagePack.read(data, DummyMessage.class);

        assertDummyMessageEquals(dummyMessage, readMessage);
    }

    @Test
    public void canWriteAndReadManyMessages() throws IOException {
        DummyMessage message1 = makeDummyMessage(0);
        DummyMessage message2 = makeDummyMessage(1);
        DummyMessage message3 = makeDummyMessage(2);

        MessagePack messagePack = new MessagePack();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Packer packer = messagePack.createPacker(baos);
        packer.write(message1);
        packer.write(message2);
        packer.write(message3);
        byte[] data = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Unpacker unpacker = messagePack.createUnpacker(bais);
        DummyMessage readMessage1 = unpacker.read(DummyMessage.class);
        DummyMessage readMessage2 = unpacker.read(DummyMessage.class);
        DummyMessage readMessage3 = unpacker.read(DummyMessage.class);

        assertDummyMessageEquals(message1, readMessage1);
        assertDummyMessageEquals(message2, readMessage2);
        assertDummyMessageEquals(message3, readMessage3);
    }

    private static DummyMessage makeDummyMessage(int seed) {
        DummyMessage dummyMessage = new DummyMessage();
        dummyMessage.stringValue = "hello there" + seed;
        dummyMessage.doubleValue = 3.14 + seed;
        return dummyMessage;
    }

    private static void assertDummyMessageEquals(DummyMessage expected, DummyMessage actual) {
        assertEquals(expected.stringValue, actual.stringValue);
        assertEquals(expected.doubleValue, actual.doubleValue, 0.001);
    }

    @Message
    public static class DummyMessage {
        public String stringValue;
        public double doubleValue;

        @Override
        public String toString() {
            return String.format("Message{%s,%f}", stringValue, doubleValue);
        }
    }
}
