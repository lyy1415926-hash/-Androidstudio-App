package com.example.campusinfoapp.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    /** 插入新用户 */
    @Insert
    void insertUser(User user);

    /** 更新已有用户信息 */
    @Update
    void updateUser(User user);

    /** 删除指定用户 */
    @Delete
    void deleteUser(User user);

    /** 登录验证 */
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    /** 根据用户名查询用户 */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    /** 查询所有用户 */
    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    /** 检查用户名是否存在，返回数量 */
    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int checkUsernameExists(String username);
}
