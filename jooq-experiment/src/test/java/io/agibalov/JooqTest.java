package io.agibalov;

import io.agibalov.db.routines.Addnumbers;
import io.agibalov.db.tables.records.SchoolsRecord;
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

import static io.agibalov.db.Tables.SCHOOLS;
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
        dslContext.insertInto(SCHOOLS)
                .columns(SCHOOLS.ID, SCHOOLS.NAME)
                .values(schoolId, schoolName)
                .execute();

        String retrievedSchoolName = dslContext.selectFrom(SCHOOLS)
                .where(SCHOOLS.ID.eq(schoolId))
                .fetchOne(SCHOOLS.NAME);
        assertEquals(schoolName, retrievedSchoolName);
    }

    @Test
    public void canUseGeneratedRecordClasses() {
        String schoolId = UUID.randomUUID().toString();
        SchoolsRecord schoolsRecord = new SchoolsRecord();
        schoolsRecord.setId(schoolId);
        schoolsRecord.setName(String.format("School %s", schoolId));
        dslContext.executeInsert(schoolsRecord);

        SchoolsRecord retrievedSchoolsRecord = dslContext.selectFrom(SCHOOLS)
                .where(SCHOOLS.ID.eq(schoolId))
                .fetchOne();
        assertEquals(schoolsRecord.getName(), retrievedSchoolsRecord.getName());
    }

    @Test
    public void canCallAStoredProcedure() {
        Addnumbers addnumbers = new Addnumbers();
        addnumbers.setA(2);
        addnumbers.setB(3);
        addnumbers.execute(dslContext.configuration());
        Field<Long> resultField = DSL.field(DSL.name("aAndB"), SQLDataType.BIGINT);
        Long result = addnumbers.getResults().get(0).getValue(0, resultField);
        assertEquals(5L, result);
    }
}
