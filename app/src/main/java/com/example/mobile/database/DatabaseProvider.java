package com.example.mobile.database;

import android.content.Context;
import androidx.room.Room;
import java.io.File;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseProvider {
    private static volatile PetCareDatabase INSTANCE;

    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static PetCareDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PetCareDatabase.class) {
                if (INSTANCE == null) {
                    // Create a path for the exported schema
                    File schemaDir = new File(context.getFilesDir(), "room_schemas");
                    if (!schemaDir.exists()) {
                        schemaDir.mkdirs(); // Create the directory if it does not exist
                    }

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PetCareDatabase.class, "pet_care_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }

    public static void clearAllData() {
        databaseWriteExecutor.execute(() -> {
            if (INSTANCE != null) {
                INSTANCE.clearAllTables();
            }
        });
    }
}
