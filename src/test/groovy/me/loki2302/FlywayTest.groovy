package me.loki2302

import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.hsqldb.jdbc.JDBCDataSource
import org.junit.Test

import javax.sql.DataSource
import static org.junit.Assert.*

class FlywayTest {
    @Test
    void dummy() {
        DataSource dataSource = new JDBCDataSource(url: 'jdbc:hsqldb:mem:testdb')

        def sql = Sql.newInstance(dataSource)

        // v1 - initial version: Posts::title is nullable
        def flyway = new Flyway()
        flyway.setDataSource(dataSource)
        flyway.setTarget('1')
        flyway.migrate()

        def appliedMigrations = flyway.info().applied()
        assertEquals(1, appliedMigrations.size())
        assertEquals('1', appliedMigrations.first().version.version)
        assertEquals('Initial version', appliedMigrations.first().description)

        sql.execute "insert into Posts(title) values('post one')"
        sql.execute 'insert into Posts(title) values(null)'
        assertEquals(2, sql.rows('select * from Posts').size())
        assertEquals(1, sql.rows('select * from Posts where title is null').size())

        // v2: Posts::title is not nullable
        flyway.setTarget('2')
        flyway.migrate()
        appliedMigrations = flyway.info().applied()
        assertEquals(2, appliedMigrations.size())
        assertEquals('2', appliedMigrations.last().version.version)
        assertEquals('Add not null constraint to post title', appliedMigrations.last().description)

        assertEquals(2, sql.rows('select * from Posts').size())
        assertEquals(0, sql.rows('select * from Posts where title is null').size())
    }
}
