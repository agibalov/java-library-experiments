package me.loki2302

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

import static org.junit.Assert.*

class CreateNullableColumnTest extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('db/create_nullable_column')
        flyway.setDataSource(dataSource)
        flyway.setTarget('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        InformationSchemaUtils.assertColumnDoesNotExist(sql, 'table2', 'data2')

        sql.executeInsert('insert into table1(data) values(?)', ['hello'])

        flyway.setTarget('2')
        flyway.migrate()

        InformationSchemaUtils.assertColumnExists(sql, 'table1', 'data2')

        def rows = sql.rows('select * from table1')
        assertEquals(1, rows.size())

        assertNull(rows[0]['data2'])
    }
}
