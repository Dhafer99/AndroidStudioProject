package com.example.mobile.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mobile.Converter.RoleConverter;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String name;
    private String email;
    private String phoneNumber;

    private String type ;

    private String password ;

    private String role  ;

    private String userType; // "Doctor" or "User"

    private Boolean isBlocked ;
    // Getters and setters
    private double latitude;
    private double longitude;

    private String image ;

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    // Getters and Setters for new fields
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String type) {
        this.userType = type;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public Boolean getIsBlocked() { return isBlocked; }

    public void setIsBlocked(Boolean isBlocked) { this.isBlocked = isBlocked; }


    public void setUserId(int userId) { this.userId = userId; }

    public int getUserId() {return userId ;} ;

    public String getRole(){
        return role ;
    }
    public void setRole(String role){
       this.role=role ;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
