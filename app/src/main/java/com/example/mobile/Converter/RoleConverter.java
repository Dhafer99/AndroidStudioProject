package com.example.mobile.Converter;

import androidx.room.TypeConverter;

import com.example.mobile.database.Type;

public class RoleConverter {

    @TypeConverter
    public static Type fromString(String value) {
        return value == null ? null : Type.valueOf(value);
    }

    @TypeConverter
    public static String roleToString(Type type) {
        return type == null ? null : type.name();
    }
}
