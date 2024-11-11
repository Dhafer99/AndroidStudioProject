package com.example.mobile.database;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(
        tableName = "appointments",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "userId",
                childColumns = "ownerId",
                onDelete = ForeignKey.CASCADE // Optional: set cascading delete
        ),
        indices = {@Index(value = "ownerId")} // Add this line to index ownerId
)



public class AppointmentEntity  implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int appointmentId;
    private String date;
    private String place;
    private int duration;
    private int ownerId; // Foreign key to User



    public AppointmentEntity() {
    }

    protected AppointmentEntity(Parcel in) {
        appointmentId = in.readInt();
        date = in.readString();
        place = in.readString();
        duration = in.readInt();
        ownerId = in.readInt();
    }

    public static final Parcelable.Creator<AnimalEntity> CREATOR = new Parcelable.Creator<AnimalEntity>() {
        @Override
        public AnimalEntity createFromParcel(Parcel in) {
            return new AnimalEntity(in);
        }

        @Override
        public AnimalEntity[] newArray(int size) {
            return new AnimalEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(appointmentId);
        dest.writeString(date);
        dest.writeString(place);
        dest.writeInt(duration);
        dest.writeInt(ownerId);
    }

    // Getters and setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

}
