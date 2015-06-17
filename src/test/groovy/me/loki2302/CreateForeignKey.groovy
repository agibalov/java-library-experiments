package me.loki2302

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class CreateForeignKey extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('db/create_foreign_key')
        flyway.setDataSource(dataSource)
        flyway.setTarget('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        assertColumnDoesNotExist(sql, 'comments', 'postId')
        assertReferenceDoesNotExist(sql, 'posts', 'id', 'comments', 'postid')

        flyway.setTarget('2')
        flyway.migrate()

        assertColumnExists(sql, 'comments', 'postId')
        assertReferenceExists(sql, 'posts', 'id', 'comments', 'postid')
    }

    void assertReferenceExists(
            Sql sql,
            String pkTableName, String pkColumnName,
            String fkTableName, String fkColumnName) {

        assertTrue(doesReferenceExist(sql, pkTableName, pkColumnName, fkTableName, fkColumnName))
    }

    void assertReferenceDoesNotExist(
            Sql sql,
            String pkTableName, String pkColumnName,
            String fkTableName, String fkColumnName) {

        assertFalse(doesReferenceExist(sql, pkTableName, pkColumnName, fkTableName, fkColumnName))
    }

    boolean doesReferenceExist(
            Sql sql,
            String pkTableName, String pkColumnName,
            String fkTableName, String fkColumnName) {

        def countRow = sql.firstRow """
select count(*) as c
from INFORMATION_SCHEMA.SYSTEM_CROSSREFERENCE
where
PKTABLE_NAME=${pkTableName.toUpperCase()} and
PKCOLUMN_NAME=${pkColumnName.toUpperCase()} and
FKTABLE_NAME=${fkTableName.toUpperCase()} and
FKCOLUMN_NAME=${fkColumnName.toUpperCase()}
"""
        countRow.c == 1
    }

    void assertColumnExists(Sql sql, String tableName, String columnName) {
        assertTrue(doesColumnExist(sql, tableName, columnName))
    }

    void assertColumnDoesNotExist(Sql sql, String tableName, String columnName) {
        assertFalse(doesColumnExist(sql, tableName, columnName))
    }

    boolean doesColumnExist(Sql sql, String tableName, String columnName) {
        def countRow = sql.firstRow """
select count(*) as c
from INFORMATION_SCHEMA.COLUMNS
where TABLE_NAME=${tableName.toUpperCase()} and COLUMN_NAME=${columnName.toUpperCase()}"""

        countRow.c == 1
    }
}
