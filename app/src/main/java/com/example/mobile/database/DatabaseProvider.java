package com.example.mobile.database;
import android.content.Context;
import androidx.room.Room;

public class DatabaseProvider {
    private static volatile PetCareDatabase INSTANCE;

    public static PetCareDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PetCareDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PetCareDatabase.class, "pet_care_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
