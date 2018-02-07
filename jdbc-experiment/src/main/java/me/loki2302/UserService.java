package me.loki2302;

import java.util.List;

public interface UserService {
	UserDTO createUser(String name);
	UserDTO getUser(int userId);
	UserDTO updateUser(int userId, String userName);
	void deleteUser(int userId);
	List<UserDTO> getAllUsers();
	PageDTO<UserDTO> getUsers(int itemsPerPage, int page);
}