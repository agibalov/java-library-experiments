package me.loki2302;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApacheDBUtilsUserService implements UserService {
    private final Connection connection;

    public ApacheDBUtilsUserService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserDTO createUser(String name) {
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(connection, "insert into Users(name) values(?)", name);
            return queryRunner.query(connection, "select id, name from Users where name = ?", new SingleUserResultSetHandler(), name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO getUser(int userId) {
        QueryRunner queryRunner = new QueryRunner();
        try {
            return queryRunner.query(connection, "select id, name from Users where id = ?", new SingleUserResultSetHandler(), userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO updateUser(int userId, String userName) {
        throw new RuntimeException();
    }

    @Override
    public void deleteUser(int userId) {
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(connection, "delete from Users where id = ?", userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        QueryRunner queryRunner = new QueryRunner();
        try {
            return queryRunner.query(connection, "select id, name from Users", new ListOfUsersResultSetHandler());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageDTO<UserDTO> getUsers(int itemsPerPage, int page) {
        QueryRunner queryRunner = new QueryRunner();
        try {
            long rowCount = queryRunner.query(connection, "select count(*) from Users", new ScalarHandler<Long>());
            int firstRow = page * itemsPerPage;
            if(rowCount <= firstRow) {
                throw new RuntimeException();
            }

            List<UserDTO> users = queryRunner.query(connection, "select limit ? ? id, name from Users", new ListOfUsersResultSetHandler(), firstRow, itemsPerPage);
            PageDTO<UserDTO> pageDto = new PageDTO<UserDTO>();
            pageDto.totalItems = (int)rowCount;
            pageDto.pageNumber = page;
            pageDto.items = users;
            return pageDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class SingleUserResultSetHandler implements ResultSetHandler<UserDTO> {
        @Override
        public UserDTO handle(ResultSet resultSet) throws SQLException {
            if(!resultSet.next()) {
                throw new RuntimeException();
            }

            UserDTO user = new UserDTO();
            user.userId = resultSet.getInt(1);
            user.userName = resultSet.getString(2);

            return user;
        }
    }

    private static class ListOfUsersResultSetHandler implements ResultSetHandler<List<UserDTO>> {
        @Override
        public List<UserDTO> handle(ResultSet resultSet) throws SQLException {
            List<UserDTO> users = new ArrayList<UserDTO>();

            while(resultSet.next()) {
                UserDTO user = new UserDTO();
                user.userId = resultSet.getInt(1);
                user.userName = resultSet.getString(2);
                users.add(user);
            }

            return users;
        }
    }
}
