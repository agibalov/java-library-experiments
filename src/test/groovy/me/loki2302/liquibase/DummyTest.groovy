package me.loki2302.liquibase

import groovy.sql.Sql
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.hsqldb.jdbc.JDBCDataSource
import org.junit.Test

import static org.junit.Assert.assertEquals

class DummyTest {
    @Test
    void itShouldWork() {
        def databaseName = UUID.randomUUID().toString()
        def dataSource = new JDBCDataSource(url: "jdbc:hsqldb:mem:$databaseName")

        def database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()))
        def liquibase = new Liquibase('liquibase/migrations.xml', new ClassLoaderResourceAccessor(), database)
        liquibase.update('')

        def sql = new Sql(dataSource)
        sql.execute("insert into notes(id, text) values(1, 'qqq1')")
        sql.execute("insert into notes(id, text) values(2, 'qqq2')")
        def rows = sql.rows('select * from notes')
        assertEquals(2, rows.size())
    }
}
