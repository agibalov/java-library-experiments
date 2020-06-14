package io.agibalov;

import io.agibalov.db.routines.AddNumbers;
import io.agibalov.db.tables.records.Schools;
import io.agibalov.tools.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import io.agibalov.db.Tables;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@Slf4j
public class JooqTest {
    @Autowired
    private DSLContext dslContext;

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

    @Test
    public void canUseInsertSelect() {
        String schoolId = UUID.randomUUID().toString();
        String schoolName = String.format("School %s", schoolId);
        dslContext.insertInto(Tables.Schools)
                .columns(Tables.Schools.id, Tables.Schools.name)
                .values(schoolId, schoolName)
                .execute();

        String retrievedSchoolName = dslContext.selectFrom(Tables.Schools)
                .where(Tables.Schools.id.eq(schoolId))
                .fetchOne(Tables.Schools.name);
        assertEquals(schoolName, retrievedSchoolName);
    }

    @Test
    public void canUseGeneratedRecordClasses() {
        String schoolId = UUID.randomUUID().toString();
        Schools schoolsRecord = new Schools();
        schoolsRecord.id(schoolId);
        schoolsRecord.name(String.format("School %s", schoolId));
        dslContext.executeInsert(schoolsRecord);

        Schools retrievedSchoolsRecord = dslContext.selectFrom(Tables.Schools)
                .where(Tables.Schools.id.eq(schoolId))
                .fetchOne();
        assertEquals(schoolsRecord.name(), retrievedSchoolsRecord.name());
    }

    @Test
    public void canCallAStoredProcedure() {
        AddNumbers addNumbers = new AddNumbers();
        addNumbers.a(2);
        addNumbers.b(3);
        addNumbers.execute(dslContext.configuration());
        Field<Long> resultField = DSL.field(DSL.name("aAndB"), SQLDataType.BIGINT);
        Long result = addNumbers.getResults().get(0).getValue(0, resultField);
        assertEquals(5L, result);
    }
}
