package io.agibalov;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DslContextTest {
    @Test
    public void canBuildTheCreateTableSqlQuery() {
        DSLContext dslContext = DSL.using(SQLDialect.MYSQL);

        String sql = dslContext.createTable("Users")
                .column("id", SQLDataType.INTEGER.nullable(false))
                .column("name", SQLDataType.VARCHAR(255).nullable(false))
                .constraint(DSL.primaryKey("id"))
                .getSQL();
        assertEquals(
                "create table `Users`(`id` int not null, `name` varchar(255) not null, primary key (`id`))",
                sql);
    }
}
