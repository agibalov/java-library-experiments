package me.loki2302
import groovy.sql.Sql

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

public class InformationSchemaUtils {
    static void assertReferenceExists(
            Sql sql,
            String pkTableName, String pkColumnName,
            String fkTableName, String fkColumnName) {

        assertTrue(doesReferenceExist(sql, pkTableName, pkColumnName, fkTableName, fkColumnName))
    }

    static void assertReferenceDoesNotExist(
            Sql sql,
            String pkTableName, String pkColumnName,
            String fkTableName, String fkColumnName) {

        assertFalse(doesReferenceExist(sql, pkTableName, pkColumnName, fkTableName, fkColumnName))
    }

    static boolean doesReferenceExist(
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

    static void assertColumnExists(Sql sql, String tableName, String columnName) {
        assertTrue(doesColumnExist(sql, tableName, columnName))
    }

    static void assertColumnDoesNotExist(Sql sql, String tableName, String columnName) {
        assertFalse(doesColumnExist(sql, tableName, columnName))
    }

    static boolean doesColumnExist(Sql sql, String tableName, String columnName) {
        def countRow = sql.firstRow """
select count(*) as c
from INFORMATION_SCHEMA.COLUMNS
where TABLE_NAME=${tableName.toUpperCase()} and COLUMN_NAME=${columnName.toUpperCase()}"""

        countRow.c == 1
    }

    static void assertIndexExists(Sql sql, String tableName, String indexName) {
        assertTrue(doesIndexExist(sql, tableName, indexName))
    }

    static void assertIndexDoesNotExist(Sql sql, String tableName, String indexName) {
        assertFalse(doesIndexExist(sql, tableName, indexName))
    }

    static boolean doesIndexExist(Sql sql, String tableName, String indexName) {
        def countRow = sql.firstRow """
select count(*) as c
from INFORMATION_SCHEMA.SYSTEM_INDEXINFO
where TABLE_NAME=${tableName.toUpperCase()} and INDEX_NAME=${indexName.toUpperCase()}"""

        countRow.c == 1
    }

    static void assertTableExists(Sql sql, String tableName) {
        assertTrue(doesTableExist(sql, tableName))
    }

    static void assertTableDoesNotExist(Sql sql, String tableName) {
        assertFalse(doesTableExist(sql, tableName))
    }

    static boolean doesTableExist(Sql sql, String tableName) {
        def countRow = sql.firstRow """
select count(*) as c
from INFORMATION_SCHEMA.TABLES
where TABLE_NAME=${tableName.toUpperCase()}"""

        countRow.c == 1
    }
}
