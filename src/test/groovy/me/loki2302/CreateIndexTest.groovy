package me.loki2302

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

class CreateIndexTest extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('db/create_index')
        flyway.setDataSource(dataSource)
        flyway.setTarget('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        InformationSchemaUtils.assertIndexDoesNotExist(sql, "table1", "dataIndex")

        flyway.setTarget('2')
        flyway.migrate()

        InformationSchemaUtils.assertIndexExists(sql, "table1", "dataIndex")
    }
}
