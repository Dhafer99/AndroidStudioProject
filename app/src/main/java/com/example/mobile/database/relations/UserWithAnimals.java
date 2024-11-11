package com.example.mobile.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mobile.database.AnimalEntity;
import com.example.mobile.database.UserEntity;

import java.util.List;

public class UserWithAnimals {
    @Embedded
    public UserEntity user;

    @Relation(
            parentColumn = "userId",
            entityColumn = "ownerId"
    )
    public List<AnimalEntity> animals;
}