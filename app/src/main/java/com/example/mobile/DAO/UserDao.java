package com.example.mobile.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobile.database.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users")
    List<UserEntity> getAllUsers();

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity loginUser(String email, String password);

    @Query("SELECT * FROM users WHERE type = 'Veterinarian'")
    List<UserEntity> getVeterinarianUsers();

    @Query("SELECT * FROM users WHERE type = 'User'")
    List<UserEntity> getNormalUserList();


    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE userId = :id LIMIT 1")
    UserEntity getUserById(Integer id);

    @Query("SELECT role FROM users WHERE userId = :id LIMIT 1")
    String getUserRoleById(Integer id);

    @Query("SELECT type FROM users WHERE type = :type LIMIT 1")
    String getUserTypeById(Integer type);





    @Query("UPDATE users SET isBlocked = :isBlocked WHERE userId = :userId")
    void updateUserBlockStatus(int userId, boolean isBlocked);



    @Update
    void updateUser(UserEntity user);


}




