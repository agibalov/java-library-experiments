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
}
