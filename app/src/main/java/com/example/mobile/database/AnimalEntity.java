package com.example.mobile.database;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "animals",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "userId",
                childColumns = "ownerId",
                onDelete = ForeignKey.CASCADE // Optional: set cascading delete
        ),
        indices = {@Index(value = "ownerId")} // Add this line to index ownerId
)
public class AnimalEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int animalId;
    private String name;
    private String species;
    private int age;
    private int ownerId; // Foreign key to User
    private String imageUri; // Field to store the image URI

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }


    public AnimalEntity() {
    }

    protected AnimalEntity(Parcel in) {
        animalId = in.readInt();
        name = in.readString();
        species = in.readString();
        age = in.readInt();
        ownerId = in.readInt();
    }

    public static final Creator<AnimalEntity> CREATOR = new Creator<AnimalEntity>() {
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
        dest.writeInt(animalId);
        dest.writeString(name);
        dest.writeString(species);
        dest.writeInt(age);
        dest.writeInt(ownerId);
    }

    // Getters and setters
    public int getAnimalId() { return animalId; }
    public void setAnimalId(int animalId) { this.animalId = animalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
}
