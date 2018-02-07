package me.loki2302;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class PureJDBCUserService implements UserService {
	private final Connection connection;
	
	public PureJDBCUserService(Connection connection) {
		this.connection = connection;
	}
	
	public UserDTO createUser(String userName) {
		try {
			PreparedStatement preparedStatement = 
					connection.prepareStatement(
							"insert into Users(name) values(?)", 
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, userName);
			preparedStatement.executeUpdate();
            connection.commit();
                       
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(!generatedKeys.next()) {
            	throw new RuntimeException();
            }
            
            int id = generatedKeys.getInt(1);
            
            UserDTO userDto = new UserDTO();
            userDto.userId = id;
            userDto.userName = userName;
            return userDto;
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}            
	}
	
	public UserDTO getUser(int userId) {
		try {
    		PreparedStatement preparedStatement = 
    				connection.prepareStatement("select id, name from Users where id = ?");
    		preparedStatement.setInt(1, userId);
    		ResultSet resultSet = preparedStatement.executeQuery();
    		
    		if(!resultSet.next()) {
    			throw new RuntimeException("no such user");
    		}
    		
    		UserDTO userDto = new UserDTO();
    		userDto.userId = resultSet.getInt("id");
    		userDto.userName = resultSet.getString("name");
    		return userDto;
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public UserDTO updateUser(int userId, String userName) {
		try {
			PreparedStatement preparedStatement = 
					connection.prepareStatement("update Users set name = ? where id = ?");
			preparedStatement.setString(1, userName);
			preparedStatement.setInt(2, userId);
			int rowsUpdated = preparedStatement.executeUpdate();
			if(rowsUpdated == 0) {
				throw new RuntimeException("no such user");
			}
			connection.commit();
			
			UserDTO userDto = new UserDTO();
    		userDto.userId = userId;
    		userDto.userName = userName;
    		return userDto;
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public void deleteUser(int userId) {
		try {
			PreparedStatement preparedStatement = 
					connection.prepareStatement("delete from Users where id = ?");
			preparedStatement.setInt(1, userId);
			int rowsUpdated = preparedStatement.executeUpdate();
			if(rowsUpdated == 0) {
				throw new RuntimeException("no such user");
			}
			connection.commit();
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public List<UserDTO> getAllUsers() {
		try {
			PreparedStatement preparedStatement = 
					connection.prepareStatement("select id, name from Users");
			ResultSet resultSet = preparedStatement.executeQuery();
			
			List<UserDTO> userDtos = new ArrayList<UserDTO>();
			while(resultSet.next()) {
				UserDTO userDto = new UserDTO();
	    		userDto.userId = resultSet.getInt("id");
	    		userDto.userName = resultSet.getString("name");
	    		userDtos.add(userDto);
			}
			return userDtos;
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public PageDTO<UserDTO> getUsers(int itemsPerPage, int page) {
		try {
			PreparedStatement countStatement = 
					connection.prepareStatement("select count(*) from Users");
			ResultSet countResultSet = countStatement.executeQuery();
			if(!countResultSet.next()) {
				throw new RuntimeException();
			}
			
			int rowCount = countResultSet.getInt(1);
			int firstRow = page * itemsPerPage;
			if(rowCount <= firstRow) {
				throw new RuntimeException();
			}			
			
			PreparedStatement preparedStatement = 
					connection.prepareStatement("select limit ? ? id, name from Users");
			preparedStatement.setInt(1, firstRow);
			preparedStatement.setInt(2, itemsPerPage);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			List<UserDTO> userDtos = new ArrayList<UserDTO>();
			while(resultSet.next()) {
				UserDTO userDto = new UserDTO();
	    		userDto.userId = resultSet.getInt("id");
	    		userDto.userName = resultSet.getString("name");
	    		userDtos.add(userDto);
			}
			
			PageDTO<UserDTO> pageDto = new PageDTO<UserDTO>();
			pageDto.totalItems = rowCount;
			pageDto.pageNumber = page;
			pageDto.items = userDtos;    			
			return pageDto;
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}    	
}