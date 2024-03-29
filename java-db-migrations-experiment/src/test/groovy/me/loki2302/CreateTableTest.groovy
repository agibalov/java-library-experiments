package me.loki2302

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

class CreateTableTest extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('flyway/create_table')
        flyway.setDataSource(dataSource)
        flyway.setTargetAsString('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        InformationSchemaUtils.assertTableExists(sql, 'table1')
        InformationSchemaUtils.assertTableDoesNotExist(sql, 'table2')

        flyway.setTargetAsString('2')
        flyway.migrate()

        InformationSchemaUtils.assertTableExists(sql, 'table1')
        InformationSchemaUtils.assertTableExists(sql, 'table2')
    }
}
