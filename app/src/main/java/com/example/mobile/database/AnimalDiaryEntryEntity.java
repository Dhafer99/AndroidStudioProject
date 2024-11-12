package com.example.mobile.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(
        tableName = "animal_diary_entries",
        foreignKeys = @ForeignKey(
                entity = AnimalEntity.class,
                parentColumns = "animalId",
                childColumns = "animalId",
                onDelete = ForeignKey.CASCADE
        )
)
public class AnimalDiaryEntryEntity {
    @PrimaryKey(autoGenerate = true)
    private int entryId;
    private int animalId;
    private String date; // Store date in yyyy-MM-dd format
    private String mood; // Can store emoji Unicode or description
    private String notes;
    private List<String> tags; // Tags like 'Playful', 'Energetic', etc.
    private List<String> images;


}
