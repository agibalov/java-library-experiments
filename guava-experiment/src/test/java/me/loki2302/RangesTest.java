package me.loki2302;

import com.google.common.collect.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RangesTest {
    @Test
    public void dummy() {
        Range r1 = Range.closed(10, 20);
        Range r2 = Range.closed(15, 17);
        assertTrue(r1.encloses(r2));
        assertEquals(r2, r1.intersection(r2));
    }
}
