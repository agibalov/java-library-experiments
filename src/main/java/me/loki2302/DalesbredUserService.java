package me.loki2302;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.evident.dalesbred.Database;
import fi.evident.dalesbred.instantiation.DefaultInstantiatorRegistry;
import fi.evident.dalesbred.results.ReflectionResultSetProcessor;
import fi.evident.dalesbred.results.ResultSetProcessor;
import fi.evident.dalesbred.results.UniqueResultSetProcessor;

public class DalesbredUserService implements UserService {
	private final Database database;
	
	public DalesbredUserService(Database database) {
		this.database = database;
	}	
	
	public UserDTO createUser(String name) {
        int userId = database.updateAndProcessGeneratedKeys(
                makeSingleIdResultSetProcessor(),
                Collections.<String>emptyList(),
                "insert into Users(name) values(?)",
                name);

		UserRow userRow = database.findUnique(
				UserRow.class,
				"select id, name from Users where id = ?",
				userId);
		return userDtoFromUserRow(userRow);
	}

    private ResultSetProcessor<Integer> makeSingleIdResultSetProcessor() {
        return UniqueResultSetProcessor.unique(
                new ReflectionResultSetProcessor<Integer>(
                        Integer.class,
                        (DefaultInstantiatorRegistry)database.getInstantiatorRegistry()));
    }

	public UserDTO getUser(int userId) {
		UserRow userRow = database.findUnique(
				UserRow.class, 
				"select id, name from Users where id = ?", 
				userId);
		return userDtoFromUserRow(userRow); 
	}

	public UserDTO updateUser(int userId, String userName) {
		database.update("update Users set name = ? where id = ?", userName, userId);
		return getUser(userId);		
	}

	public void deleteUser(int userId) {
		database.update("delete from Users where id = ?", userId);
	}

	public List<UserDTO> getAllUsers() {
		List<UserRow> userRows = database.findAll(
				UserRow.class, 
				"select id, name from Users");
		return userDtosFromUserRows(userRows);
	}

	public PageDTO<UserDTO> getUsers(int itemsPerPage, int page) {
		int rowCount = database.findUnique(
				Integer.class, 
				"select count(*) from Users");		
		int firstRow = page * itemsPerPage;
		if(rowCount <= firstRow) {
			throw new RuntimeException();
		}
		
		List<UserRow> userRows = 
				database.findAll(
						UserRow.class, "select limit ? ? id, name from Users", 
						firstRow, 
						itemsPerPage);
		
		List<UserDTO> userDtos = userDtosFromUserRows(userRows);
		PageDTO<UserDTO> pageDto = new PageDTO<UserDTO>();
		pageDto.totalItems = rowCount;
		pageDto.pageNumber = page;
		pageDto.items = userDtos;    			
		return pageDto;
	}
	
	private static UserDTO userDtoFromUserRow(UserRow userRow) {
		UserDTO userDto = new UserDTO();
		userDto.userId = userRow.userId;
		userDto.userName = userRow.userName;
		return userDto;
	}
	
	private static List<UserDTO> userDtosFromUserRows(List<UserRow> userRows) {
		List<UserDTO> userDtos = new ArrayList<UserDTO>();
		for(UserRow userRow : userRows) {
			UserDTO userDto = userDtoFromUserRow(userRow);
			userDtos.add(userDto);
		}
		return userDtos;
	}
	
	public static class UserRow {
		private final int userId;
		private final String userName;
		
		public UserRow(int userId, String userName) {
			this.userId = userId;
			this.userName = userName;
		}
	}
}