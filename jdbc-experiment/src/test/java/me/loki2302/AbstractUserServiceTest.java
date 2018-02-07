package me.loki2302;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public abstract class AbstractUserServiceTest {
	protected UserService userService;
	
	@Test
	public void thereAreNoUsersByDefault() {
		List<UserDTO> users = userService.getAllUsers();
		assertEquals(0, users.size());
	}
	
	@Test
	public void canCreateUser() {
		UserDTO userDto = userService.createUser("loki2302");
		assertEquals(0, userDto.userId);
		assertEquals("loki2302", userDto.userName);
		
		List<UserDTO> users = userService.getAllUsers();
		assertEquals(1, users.size());
	}
	
	@Test
	public void canDeleteUser() {
		UserDTO userDto = userService.createUser("loki2302");
		userService.deleteUser(userDto.userId);
		List<UserDTO> users = userService.getAllUsers();
		assertEquals(0, users.size());
	}
	
	@Test
	public void canGetUser() {
		UserDTO userDto = userService.createUser("loki2302");
		UserDTO userDto2 = userService.getUser(userDto.userId);
		assertEquals(userDto.userId, userDto2.userId);
		assertEquals(userDto.userName, userDto2.userName);
	}
	
	@Test
	public void canGetAllUsers() {
		for(int i = 0; i < 100; ++i) {
			userService.createUser(String.format("user_%d", i));
		}
		
		List<UserDTO> userDtos = userService.getAllUsers();
		assertEquals(100, userDtos.size());
		for(int i = 0; i < 100; ++i) {
			UserDTO userDto = userDtos.get(i);
			assertEquals(String.format("user_%d", i), userDto.userName);
		}
	}
	
	@Test
	public void canGetAllUsersWithPagination() {
		for(int i = 0; i < 10; ++i) {
			userService.createUser(String.format("user_%d", i));
		}
		
		PageDTO<UserDTO> page1Dto = userService.getUsers(3, 0);
		assertEquals(10, page1Dto.totalItems);
		assertEquals(0, page1Dto.pageNumber);
		assertEquals(3, page1Dto.items.size());
		assertEquals("user_0", page1Dto.items.get(0).userName);
		assertEquals("user_1", page1Dto.items.get(1).userName);
		assertEquals("user_2", page1Dto.items.get(2).userName);
		
		PageDTO<UserDTO> page2Dto = userService.getUsers(3, 1);
		assertEquals(10, page2Dto.totalItems);
		assertEquals(1, page2Dto.pageNumber);
		assertEquals(3, page2Dto.items.size());
		assertEquals("user_3", page2Dto.items.get(0).userName);
		assertEquals("user_4", page2Dto.items.get(1).userName);
		assertEquals("user_5", page2Dto.items.get(2).userName);
		
		PageDTO<UserDTO> page3Dto = userService.getUsers(3, 2);
		assertEquals(10, page3Dto.totalItems);
		assertEquals(2, page3Dto.pageNumber);
		assertEquals(3, page3Dto.items.size());
		assertEquals("user_6", page3Dto.items.get(0).userName);
		assertEquals("user_7", page3Dto.items.get(1).userName);
		assertEquals("user_8", page3Dto.items.get(2).userName);
		
		PageDTO<UserDTO> page4Dto = userService.getUsers(3, 3);
		assertEquals(10, page4Dto.totalItems);
		assertEquals(3, page4Dto.pageNumber);
		assertEquals(1, page4Dto.items.size());
		assertEquals("user_9", page4Dto.items.get(0).userName);		
	}
}