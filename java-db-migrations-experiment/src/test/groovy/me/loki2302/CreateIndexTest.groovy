package me.loki2302

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

class CreateIndexTest extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('flyway/create_index')
        flyway.setDataSource(dataSource)
        flyway.setTargetAsString('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        InformationSchemaUtils.assertIndexDoesNotExist(sql, "table1", "dataIndex")

        flyway.setTargetAsString('2')
        flyway.migrate()

        InformationSchemaUtils.assertIndexExists(sql, "table1", "dataIndex")
    }
}
