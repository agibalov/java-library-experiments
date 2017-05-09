package me.loki2302;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MapDBTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void dummy() throws IOException {
        File dbFile = temporaryFolder.newFile();
        dbFile.delete();

        try(DB db = DBMaker.fileDB(dbFile).make()) {
            HTreeMap<String, String> map = (HTreeMap<String, String>) db.hashMap("mymap").createOrOpen();
            map.put("hello", "world");
        }

        try(DB db = DBMaker.fileDB(dbFile).make()) {
            HTreeMap<String, String> map = (HTreeMap<String, String>) db.hashMap("mymap").createOrOpen();
            String value = map.get("hello");
            assertEquals("world", value);
        }
    }

    @Test
    public void canUseTransactions() throws IOException {
        File dbFile = temporaryFolder.newFile();
        dbFile.delete();

        try(DB db = DBMaker.fileDB(dbFile).transactionEnable().make()) {
            HTreeMap<String, String> map = (HTreeMap<String, String>) db.hashMap("mymap").createOrOpen();
            db.commit(); // commit just created mymap
            map.put("hello", "world");
            db.rollback();
            map.put("something", "else");
            db.commit();
        }

        try(DB db = DBMaker.fileDB(dbFile).make()) {
            HTreeMap<String, String> map = (HTreeMap<String, String>) db.hashMap("mymap").createOrOpen();
            assertNull(map.get("hello"));
            assertEquals("else", map.get("something"));
        }
    }
}
