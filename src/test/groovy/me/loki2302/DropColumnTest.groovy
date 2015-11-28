package me.loki2302

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

import static org.junit.Assert.assertEquals

class DropColumnTest extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('flyway/drop_column')
        flyway.setDataSource(dataSource)
        flyway.setTargetAsString('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        InformationSchemaUtils.assertColumnExists(sql, 'table1', 'data2')

        sql.executeInsert('insert into table1(data, data2) values(?, ?)', ['hello', 123])

        flyway.setTargetAsString('2')
        flyway.migrate()

        InformationSchemaUtils.assertColumnDoesNotExist(sql, 'table1', 'data2')

        def rows = sql.rows('select * from table1')
        assertEquals(1, rows.size())

        assertEquals('hello', rows[0]['data'])
    }
}
