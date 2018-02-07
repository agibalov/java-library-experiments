package me.loki2302;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ApacheDBUtilsUserServiceTest extends AbstractUserServiceTest {
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        DbUtils.loadDriver("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:mydb");

        QueryRunner runner = new QueryRunner();
        runner.update(connection, "create table Users(id int identity, name varchar(256) not null)");

        userService = new ApacheDBUtilsUserService(connection);
    }

    @After
    public void tearDown() throws SQLException {
        QueryRunner runner = new QueryRunner();
        runner.update(connection, "drop schema public cascade");

        DbUtils.closeQuietly(connection);
    }
}
