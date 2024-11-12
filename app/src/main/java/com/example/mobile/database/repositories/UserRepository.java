package com.example.mobile.database.repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.mobile.DAO.UserDao;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.UserEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserRepository {

    private UserDao userDao;
    private final ExecutorService executorService;
    public UserRepository(Context context) {

        PetCareDatabase db = DatabaseProvider.getDatabase(context);

        userDao = db.userDao();

        executorService = Executors.newSingleThreadExecutor();
    }

    public UserEntity loginUser(String email, String password) {
        return userDao.loginUser(email, password);
    }
    public void insertuser(UserEntity user) {
         userDao.insertUser(user);
    }

    public UserEntity getUserByEmail(String email){
        return userDao.getUserByEmail(email);
    }
    public List<UserEntity> getAllUsers(){
        return userDao.getAllUsers();
    }

    public void updateUserBlockStatus(int userId, boolean isBlocked){
        userDao.updateUserBlockStatus(userId,isBlocked);
    }
    public UserEntity getUserById(Integer id){
        return userDao.getUserById(id);

    }

    public Future<List<UserEntity>> getUserListFuture() {
        return executorService.submit(userDao::getNormalUserList);
    }
    public Future<List<UserEntity>> getVeterinarianUsers() {
        return executorService.submit(userDao::getVeterinarianUsers);
    }


    // Alternatively, if updating the whole UserEntity
    public void updateUser(UserEntity user) {
        executorService.execute(() -> userDao.updateUser(user));
    }

    // In UserRepository.java
    public void updateUserPassword(String email, String newPassword) {
        executorService.execute(() -> {
            UserEntity user = userDao.getUserByEmail(email);
            if (user != null) {
                user.setPassword(newPassword); // Store plain text or hashed based on requirements
                userDao.updateUser(user); // updateUser should be implemented in your DAO
            }
        });
    }
    // Asynchronous method to get the user role by userId
    public Future<String> getUserRoleById(int userId) {
        return executorService.submit(() -> userDao.getUserRoleById(userId));
    }

    public Future<String> getUserTypeById(int userId) {
        return executorService.submit(() -> userDao.getUserTypeById(userId));
    }



}