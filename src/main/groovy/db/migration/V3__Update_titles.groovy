package db.migration

import groovy.sql.Sql
import org.flywaydb.core.api.migration.jdbc.JdbcMigration

import java.sql.Connection

class V3__Update_titles implements JdbcMigration {
    @Override
    void migrate(Connection connection) throws Exception {
        def sql = new Sql(connection)
        def rows = sql.rows("select id, title from Posts")
        rows.each { row ->
            def title = "post-${row.id}".toString()
            sql.execute("update Posts set title = ${title} where id = ${row.id}")
        }
    }
}
