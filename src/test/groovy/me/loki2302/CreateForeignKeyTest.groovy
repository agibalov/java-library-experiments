package me.loki2302
import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.junit.Test

class CreateForeignKeyTest extends AbstractFlywayTest {
    @Test
    void dummy() {
        def flyway = new Flyway()
        flyway.setLocations('db/create_foreign_key')
        flyway.setDataSource(dataSource)
        flyway.setTarget('1')
        flyway.migrate()

        Sql sql = new Sql(dataSource)
        InformationSchemaUtils.assertColumnDoesNotExist(sql, 'comments', 'postId')
        InformationSchemaUtils.assertReferenceDoesNotExist(sql, 'posts', 'id', 'comments', 'postid')

        flyway.setTarget('2')
        flyway.migrate()

        InformationSchemaUtils.assertColumnExists(sql, 'comments', 'postId')
        InformationSchemaUtils.assertReferenceExists(sql, 'posts', 'id', 'comments', 'postid')
    }
}
