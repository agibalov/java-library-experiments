package com.loki2302;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class JodaTest {
    @Test
    public void canFormatDateTimeAsText() {        
        DateTime dt = new DateTime(2013, 9, 22, 19, 38, 13);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("'on' MMMM d, yyyy 'at' h:mm:ss a");
        assertEquals("on September 22, 2013 at 7:38:13 PM", formatter.print(dt));
    }
    
    @Test
    public void canComputeDifferenceBetweenTimes() {
        DateTime dt0 = new DateTime(2013, 9, 22, 19, 38, 13);
        DateTime dt1 = new DateTime(2013, 9, 22, 19, 40, 3);
        Period period = new Period(dt0, dt1);
        assertEquals(50, period.getSeconds());
        assertEquals(1, period.getMinutes());
    }    
    
    @Test
    public void canComputeSecondsBetweenTimes() {
        DateTime dt0 = new DateTime(2013, 9, 22, 19, 38, 13);
        DateTime dt1 = new DateTime(2013, 9, 22, 19, 40, 3);
        Seconds seconds = Seconds.secondsBetween(dt0, dt1);
        assertEquals(110, seconds.getSeconds());
    }
    
    @Test
    public void canAddSeconds() {
        DateTime dt0 = new DateTime(2013, 9, 22, 19, 38, 13);
        DateTime dt1 = dt0.plusSeconds(110); 
        assertEquals(new DateTime(2013, 9, 22, 19, 40, 3), dt1);
    }
    
    @Test
    public void canSubSeconds() {
        DateTime dt0 = new DateTime(2013, 9, 22, 19, 40, 3);
        DateTime dt1 = dt0.minusSeconds(110); 
        assertEquals(new DateTime(2013, 9, 22, 19, 38, 13), dt1);
    } 
}