package me.loki2302
import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.dbsupport.FlywaySqlScriptException
import org.junit.Test

import static org.junit.Assert.*

class DummyTest extends AbstractFlywayTest {
    @Test
    void canCreateV1Database() {
        def flyway = new Flyway()
        flyway.setDataSource(dataSource)
        flyway.setTargetAsString('1')
        flyway.migrate()

        def appliedMigrations = flyway.info().applied()
        assertEquals(1, appliedMigrations.size())
        assertEquals('1', appliedMigrations.first().version.version)
        assertEquals('Initial version', appliedMigrations.first().description)

        Sql sql = new Sql(dataSource)
        assertEquals(0, sql.firstRow('select count(*) as c from Posts').c)

        sql.execute('insert into Posts(title) values(null)')
        assertEquals(1, sql.firstRow('select count(*) as c from Posts').c)
        assertEquals(1, sql.firstRow('select count(*) as c from Posts where title is null').c)
    }

    @Test
    void canCreateV2Database() {
        def flyway = new Flyway()
        flyway.setDataSource(dataSource)
        flyway.setTargetAsString('2')
        flyway.migrate()

        def appliedMigrations = flyway.info().applied()
        assertEquals(2, appliedMigrations.size())
        assertEquals('1', appliedMigrations.first().version.version)
        assertEquals('Initial version', appliedMigrations.first().description)
        assertEquals('2', appliedMigrations.last().version.version)
        assertEquals('Add not null constraint to post title', appliedMigrations.last().description)

        Sql sql = new Sql(dataSource)
        assertEquals(0, sql.firstRow('select count(*) as c from Posts').c)

        sql.execute("insert into Posts(title) values('')")
        assertEquals(1, sql.firstRow('select count(*) as c from Posts').c)
        assertEquals(0, sql.firstRow('select count(*) as c from Posts where title is null').c)
    }

    @Test
    void canUpdateV1DatabaseToV2() {
        def flyway = new Flyway()
        flyway.setDataSource(dataSource)
        flyway.setTargetAsString('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        sql.execute('insert into Posts(title) values(null)')

        flyway.setTargetAsString('2')
        flyway.migrate()

        def appliedMigrations = flyway.info().applied()
        assertEquals(2, appliedMigrations.size())
        assertEquals('1', appliedMigrations.first().version.version)
        assertEquals('Initial version', appliedMigrations.first().description)
        assertEquals('2', appliedMigrations.last().version.version)
        assertEquals('Add not null constraint to post title', appliedMigrations.last().description)

        assertEquals(1, sql.firstRow('select count(*) as c from Posts').c)
        assertEquals(0, sql.firstRow('select count(*) as c from Posts where title is null').c)
    }

    @Test
    void canUseJavaMigration() {
        def flyway = new Flyway()
        flyway.setDataSource(dataSource)
        flyway.setTargetAsString('2')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        sql.execute("insert into Posts(title) values('')")

        flyway.setTargetAsString('3')
        flyway.migrate()

        def row = sql.firstRow('select * from Posts')
        assertEquals(0, row.id)
        assertEquals('post-0', row.title)
    }

    @Test
    void canGetAnErrorWhenMigrationFails() {
        def flyway = new Flyway()
        flyway.setDataSource(dataSource)
        flyway.setTargetAsString('3')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        sql.execute("insert into Posts(title) values('')")

        flyway.setTargetAsString('4')

        try {
            flyway.migrate()
            fail()
        } catch(FlywaySqlScriptException e) {
            assertTrue(e.message.contains('V4__Bad_migration.sql'))
        }
    }
}
