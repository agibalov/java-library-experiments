package me.loki2302;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.junit.Test;

public class ArbitraryEntriesTest {
    @Test
    public void test() throws IOException {
        File archiveFile = File.createTempFile("zip", null);
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(archiveFile));
            addEntryToZipOutputStream(zipOutputStream, "1.txt", "hello");
            addEntryToZipOutputStream(zipOutputStream, "2.txt", "world");
            addEntryToZipOutputStream(zipOutputStream, "3.txt", "omg");
            zipOutputStream.close();
            
            ZipFile zipFile = new ZipFile(archiveFile);
            List<InputStream> inputStreams = getStreamsForAllEntriesInZipFile(zipFile);
            
            assertEquals("world", readStringFromInputStream(inputStreams.get(1)));
            assertEquals("hello", readStringFromInputStream(inputStreams.get(0)));
            assertEquals("omg", readStringFromInputStream(inputStreams.get(2)));
            
            zipFile.close();            
        } finally {
            archiveFile.delete();
        }
    }

    private static void addEntryToZipOutputStream(
            ZipOutputStream zipOutputStream, 
            String entryName, 
            String entryContent) throws IOException {
        
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipEntry.setSize(entryContent.length());
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(entryContent.getBytes());
        zipOutputStream.closeEntry();
    }
    
    private static String readStringFromInputStream(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        try {
            return scanner.next();
        } finally {
            scanner.close();
        }
    }
    
    private static List<InputStream> getStreamsForAllEntriesInZipFile(ZipFile zipFile) throws IOException {
        List<InputStream> inputStreams = new ArrayList<InputStream>();            
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while(entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();                
            inputStreams.add(zipFile.getInputStream(zipEntry));
            System.out.printf("%s [size:%d]\n", zipEntry.getName(), zipEntry.getSize());
        }
        return inputStreams;
    }
}