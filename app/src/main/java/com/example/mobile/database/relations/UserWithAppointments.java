package com.example.mobile.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mobile.database.AppointmentEntity;
import com.example.mobile.database.UserEntity;

import java.util.List;

public class UserWithAppointments {
    @Embedded
    public UserEntity user;

    @Relation(
            parentColumn = "userId",
            entityColumn = "ownerId"
    )
    public List<AppointmentEntity> appointments;
}
