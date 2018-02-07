package me.loki2302;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class AppTest {
    private final static int[] TEST_DATA = new int[] { 111, 222, 333 };

    @Test
    public void canWriteAndReadArrayOfIntegers() throws IOException, HDF5Exception {
        String filePath = File.createTempFile("tmp", "tmp").getCanonicalPath();
        App.writeArrayOfIntsToHDF5File(filePath, TEST_DATA);
        int[] readData = App.readArrayOfIntsFromHDF5File(filePath);
        assertArrayEquals(TEST_DATA, readData);
    }
}
