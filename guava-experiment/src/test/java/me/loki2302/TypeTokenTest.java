package me.loki2302;

import com.google.common.reflect.TypeToken;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TypeTokenTest {
    @Test
    public void dummy() {
        assertEquals(new ArrayList<String>().getClass(), new ArrayList<Integer>().getClass());

        TypeToken<ArrayList<String>> arrayListOfStringTypeToken = new TypeToken<ArrayList<String>>() {};
        TypeToken<ArrayList<String>> arrayListOfStringTypeToken2 = new TypeToken<ArrayList<String>>() {};
        assertEquals(arrayListOfStringTypeToken, arrayListOfStringTypeToken2);

        TypeToken<ArrayList<Integer>> arrayListOfIntegerTypeToken = new TypeToken<ArrayList<Integer>>() {};
        assertNotEquals(arrayListOfStringTypeToken, arrayListOfIntegerTypeToken);
    }
}
