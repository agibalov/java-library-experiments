package io.agibalov.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

@Slf4j
public class ExternalMysqlTestExecutionListener extends AbstractTestExecutionListener {
    private static final String mysqlHost = System.getenv("TEST_MYSQL_HOST");
    private static final String mysqlPort = System.getenv("TEST_MYSQL_PORT");
    private static final String mysqlRootUsername = System.getenv("TEST_MYSQL_ROOT_USERNAME");
    private static final String mysqlRootPassword = System.getenv("TEST_MYSQL_ROOT_PASSWORD");
    private static final String mysqlDatabase = System.getenv("TEST_MYSQL_DATABASE");
    private static final String mysqlUsername = System.getenv("TEST_MYSQL_USERNAME");
    private static final String mysqlPassword = System.getenv("TEST_MYSQL_PASSWORD");

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void beforeTestClass(TestContext testContext) {
        JdbcTemplate jdbcTemplate = makeJdbcTemplate();
        jdbcTemplate.execute(String.format("drop database if exists `%s`", mysqlDatabase));
        jdbcTemplate.execute(String.format("create database `%s`", mysqlDatabase));
        jdbcTemplate.execute(String.format("create user if not exists `%s`@`%%` identified by '%s'",
                mysqlUsername, mysqlPassword));
        jdbcTemplate.execute(String.format("grant all on `%s`.* to `%s`@`%%`", mysqlDatabase, mysqlUsername));
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        JdbcTemplate jdbcTemplate = makeJdbcTemplate();
        jdbcTemplate.execute(String.format("drop database `%s`", mysqlDatabase));
        jdbcTemplate.execute(String.format("drop user `%s`", mysqlUsername));
    }

    private static JdbcTemplate makeJdbcTemplate() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(
                String.format("jdbc:mysql://%s:%s", mysqlHost, mysqlPort),
                mysqlRootUsername,
                mysqlRootPassword);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(driverManagerDataSource);
        return jdbcTemplate;
    }
}
