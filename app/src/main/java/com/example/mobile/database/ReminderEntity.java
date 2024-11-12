package com.example.mobile.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class ReminderEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private String channelId;
    private long timeInMillis;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public long getTimeInMillis() { return timeInMillis; }
    public void setTimeInMillis(long timeInMillis) { this.timeInMillis = timeInMillis; }
}
