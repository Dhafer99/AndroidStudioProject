package com.example.mobile.database.repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.mobile.DAO.UserDao;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.UserEntity;

public class UserRepository {

    private UserDao userDao;

    public UserRepository(Context context) {
        PetCareDatabase db = DatabaseProvider.getDatabase(context);
        userDao = db.userDao();
    }

    public UserEntity loginUser(String email, String password) {
        return userDao.loginUser(email, password);
    }
}