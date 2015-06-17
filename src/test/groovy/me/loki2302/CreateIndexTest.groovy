package me.loki2302

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class CreateIndexTest extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('db/create_index')
        flyway.setDataSource(dataSource)
        flyway.setTarget('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        assertIndexDoesNotExist(sql, "table1", "dataIndex")

        flyway.setTarget('2')
        flyway.migrate()

        assertIndexExists(sql, "table1", "dataIndex")
    }

    void assertIndexExists(Sql sql, String tableName, String indexName) {
        assertTrue(doesIndexExist(sql, tableName, indexName))
    }

    void assertIndexDoesNotExist(Sql sql, String tableName, String indexName) {
        assertFalse(doesIndexExist(sql, tableName, indexName))
    }

    boolean doesIndexExist(Sql sql, String tableName, String indexName) {
        def countRow = sql.firstRow """
select count(*) as c
from INFORMATION_SCHEMA.SYSTEM_INDEXINFO
where TABLE_NAME=${tableName.toUpperCase()} and INDEX_NAME=${indexName.toUpperCase()}"""

        countRow.c == 1
    }
}
