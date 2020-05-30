package io.agibalov;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NullablesTest {
    @Test
    public void canDeserializeNullableInteger() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        AThingWithNullableInteger aThingWithNullableInteger = xmlMapper.readValue(
                "<root><something>123</something></root>",
                AThingWithNullableInteger.class);
        assertEquals(123, aThingWithNullableInteger.something.intValue());

        aThingWithNullableInteger = xmlMapper.readValue(
                "<root><something /></root>",
                AThingWithNullableInteger.class);
        assertNull(aThingWithNullableInteger.something);

        aThingWithNullableInteger = xmlMapper.readValue(
                "<root><something></something></root>",
                AThingWithNullableInteger.class);
        assertNull(aThingWithNullableInteger.something);
    }

    @Test
    public void canDeserializeNullableDouble() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        AThingWithNullableDouble aThingWithNullableDouble = xmlMapper.readValue(
                "<root><something>1.23</something></root>",
                AThingWithNullableDouble.class);
        assertEquals(1.23, aThingWithNullableDouble.something.doubleValue(), 0.001);

        aThingWithNullableDouble = xmlMapper.readValue(
                "<root><something /></root>",
                AThingWithNullableDouble.class);
        assertNull(aThingWithNullableDouble.something);

        aThingWithNullableDouble = xmlMapper.readValue(
                "<root><something></something></root>",
                AThingWithNullableDouble.class);
        assertNull(aThingWithNullableDouble.something);
    }

    @Test
    public void canDeserializeInheritedNullableDouble() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        InheritedThingWithNullableDouble anInheritedThingWithNullableDouble = xmlMapper.readValue(
                "<root><something>1.23</something></root>",
                InheritedThingWithNullableDouble.class);
        assertEquals(1.23, anInheritedThingWithNullableDouble.something.doubleValue(), 0.001);

        anInheritedThingWithNullableDouble = xmlMapper.readValue(
                "<root><something /></root>",
                InheritedThingWithNullableDouble.class);
        assertNull(anInheritedThingWithNullableDouble.something);

        anInheritedThingWithNullableDouble = xmlMapper.readValue(
                "<root><something></something></root>",
                InheritedThingWithNullableDouble.class);
        assertNull(anInheritedThingWithNullableDouble.something);
    }

    public static class AThingWithNullableInteger {
        public Integer something;
    }

    public static class AThingWithNullableDouble {
        public Double something;
    }

    public static abstract class AbstractGenericThing<TValue> {
        public TValue something;
    }

    public static class InheritedThingWithNullableDouble extends AbstractGenericThing<Double> {
    }
}
