package me.loki2302

import org.hsqldb.jdbc.JDBCDataSource
import org.junit.Before

import javax.sql.DataSource

abstract class AbstractFlywayTest {
    protected DataSource dataSource

    @Before
    void constructDataSource() {
        def databaseName = UUID.randomUUID().toString()
        dataSource = new JDBCDataSource(url: "jdbc:hsqldb:mem:${databaseName}")
    }
}
