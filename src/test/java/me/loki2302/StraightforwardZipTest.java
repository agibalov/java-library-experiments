package me.loki2302;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class StraightforwardZipTest {
    @Test
    public void canWriteZipFile() throws IOException {
        File zipFile = new File("1.zip");
        if(zipFile.exists()) {
            zipFile.delete();
        }
        
        writeZipFile("1.zip", "1.txt", "hello there!");
        
        assertTrue(zipFile.exists());
        assertNotEquals(0, zipFile.length());
    }
    
    @Test
    public void canReadZipFile() throws IOException {
        File zipFile = new File("1.zip");
        if(zipFile.exists()) {
            zipFile.delete();
        }
        
        writeZipFile("1.zip", "1.txt", "hello there!");
        String content = readZipFile("1.zip", "1.txt");
        
        assertEquals("hello there!", content);
    }
    
    private static void writeZipFile(String zipFileName, String contentFileName, String content) throws IOException {
        File zipFile = new File(zipFileName);        
        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        
        ZipEntry zipEntry = new ZipEntry(contentFileName);
        try {
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(content.getBytes());
        } finally {
            try {
                zipOutputStream.closeEntry();
            } catch(IOException e) {                
            } finally {
                try {
                    zipOutputStream.close();
                } catch(IOException e) {                    
                }
            }
        }        
    }
    
    private static String readZipFile(String zipFileName, String contentFileName) throws IOException {
        File zipFile = new File(zipFileName);
        FileInputStream fileInputStream = new FileInputStream(zipFile);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        ZipEntry zipEntry = null;
        while(true) {
            zipEntry = zipInputStream.getNextEntry();
            if(zipEntry == null) {
                break;
            }
            
            if(zipEntry.getName().equals(contentFileName)) {
                break;
            }
        }
        
        try {        
            if(zipEntry == null) {
                throw new RuntimeException("Reached the end of zip and didn't found anything good");
            }
            
            byte[] contentBytes = IOUtils.toByteArray(zipInputStream);
            return new String(contentBytes);
        } finally {            
            try {
                zipInputStream.closeEntry();
            } catch(IOException e) {                
            } finally {
                try {
                    zipInputStream.close();
                } catch(IOException e) {
                }
            }
        }
    }
}
