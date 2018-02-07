package me.loki2302;

public class UserDTO {
	public int userId;
	public String userName;
	
	@Override
	public String toString() {
		return String.format("User{id=%d,name=%s}", userId, userName);
	}
}