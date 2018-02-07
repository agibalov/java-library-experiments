package me.loki2302;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.dalesbred.Database;
import org.junit.After;
import org.junit.Before;

public class DalesbredUserServiceTest extends AbstractUserServiceTest {
	private Connection connection;
	
	@Before
	public void setUp() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");        
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:mydb");
        
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table Users(id int identity, name varchar(256) not null)");
        connection.commit();
        statement.close();        
        
        Database database = Database.forUrlAndCredentials("jdbc:hsqldb:mem:mydb", null, null);
        userService = new DalesbredUserService(database);
	}
	
	@After
	public void tearDown() throws ClassNotFoundException, SQLException {		        
		Statement statement = connection.createStatement();
		statement.execute("DROP SCHEMA PUBLIC CASCADE");
        connection.commit();
        statement.close();        
        connection.close();
	}
}
