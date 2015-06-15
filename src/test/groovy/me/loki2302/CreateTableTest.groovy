package me.loki2302
import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

import static org.junit.Assert.*

class CreateTableTest extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('db/create_table')
        flyway.setDataSource(dataSource)
        flyway.setTarget('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        assertTableExists(sql, 'table1')
        assertTableDoesNotExist(sql, 'table2')

        flyway.setTarget('2')
        flyway.migrate()

        assertTableExists(sql, 'table1')
        assertTableExists(sql, 'table2')
    }

    boolean assertTableExists(Sql sql, String tableName) {
        assertTrue(doesTableExist(sql, tableName))
    }

    boolean assertTableDoesNotExist(Sql sql, String tableName) {
        assertFalse(doesTableExist(sql, tableName))
    }

    boolean doesTableExist(Sql sql, String tableName) {
        def countRow = sql.firstRow """
select count(*) as c
from INFORMATION_SCHEMA.TABLES
where TABLE_NAME=${tableName.toUpperCase()}"""

        countRow.c == 1
    }
}
