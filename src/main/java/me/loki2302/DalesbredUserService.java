package me.loki2302;

import org.dalesbred.Database;
import org.dalesbred.result.ResultSetProcessor;
import org.dalesbred.result.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class DalesbredUserService implements UserService {
	private final Database database;
	
	public DalesbredUserService(Database database) {
		this.database = database;
	}	
	
	public UserDTO createUser(String name) {
		ResultSetProcessor<Integer> rpp = new ResultSetProcessor<Integer>() {
			@Override
			public Integer process(ResultSet resultSet) throws SQLException {
				if(!resultSet.next()) {
					throw new RuntimeException();
				}

				return resultSet.getInt(1);
			}
		};

        int userId = database.<Integer>updateAndProcessGeneratedKeys(
                rpp,
                Collections.<String>emptyList(),
                "insert into Users(name) values(?)",
                name);

        UserDTO userDto = database.findUnique(new UserDTOMapper(), "select id, name from Users where id = ?", userId);
        return userDto;
	}

	public UserDTO getUser(int userId) {
		return database.findUnique(new UserDTOMapper(), "select id, name from Users where id = ?", userId);
	}

	public UserDTO updateUser(int userId, String userName) {
		database.update("update Users set name = ? where id = ?", userName, userId);
		return getUser(userId);
	}

	public void deleteUser(int userId) {
		database.update("delete from Users where id = ?", userId);
	}

	public List<UserDTO> getAllUsers() {
		return database.findAll(
				new UserDTOMapper(),
				"select id, name from Users");
	}

	public PageDTO<UserDTO> getUsers(int itemsPerPage, int page) {
		int rowCount = database.findUnique(
				Integer.class, 
				"select count(*) from Users");		
		int firstRow = page * itemsPerPage;
		if(rowCount <= firstRow) {
			throw new RuntimeException();
		}
		
		List<UserDTO> userDtos = database.findAll(
                new UserDTOMapper(), "select limit ? ? id, name from Users", firstRow, itemsPerPage);

		PageDTO<UserDTO> pageDto = new PageDTO<UserDTO>();
		pageDto.totalItems = rowCount;
		pageDto.pageNumber = page;
		pageDto.items = userDtos;    			
		return pageDto;
	}

    private static class UserDTOMapper implements RowMapper<UserDTO> {
        @Override
        public UserDTO mapRow(ResultSet resultSet) throws SQLException {
            UserDTO userDto = new UserDTO();
            userDto.userId = resultSet.getInt("id");
            userDto.userName = resultSet.getString("name");
            return userDto;
        }
    }
}
