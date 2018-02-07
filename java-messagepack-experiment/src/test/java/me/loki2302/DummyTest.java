package me.loki2302;

import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void canWriteAndReadADummyMessage() throws IOException {
        DummyMessage dummyMessage = makeDummyMessage(0);

        MessagePack messagePack = new MessagePack();
        byte[] data = messagePack.write(dummyMessage);
        DummyMessage readMessage = messagePack.read(data, DummyMessage.class);

        assertDummyMessageEquals(dummyMessage, readMessage);
    }

    @Test
    public void canWriteAndReadManyDummyMessages() throws IOException {
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

        float[] floatArray = new float[65536 * 64 - 1]; // this size is the best it can do
        Arrays.fill(floatArray, seed);
        dummyMessage.floatArray = floatArray;

        return dummyMessage;
    }

    private static void assertDummyMessageEquals(DummyMessage expected, DummyMessage actual) {
        assertEquals(expected.stringValue, actual.stringValue);
        assertEquals(expected.doubleValue, actual.doubleValue, 0.001);
        assertArrayEquals(expected.floatArray, actual.floatArray, 0.001f);
    }

    @Message
    public static class DummyMessage {
        public String stringValue;
        public double doubleValue;
        public float[] floatArray;
    }
}
