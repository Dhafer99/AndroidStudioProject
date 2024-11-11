package com.example.mobile.ui.animal;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.mobile.database.AnimalEntity;
import com.example.mobile.database.relations.UserWithAnimals;
import com.example.mobile.database.repositories.AnimalRepository;
import java.util.List;

public class AnimalViewModel extends AndroidViewModel {
    private final AnimalRepository repository;
    private final LiveData<List<AnimalEntity>> allAnimals;

    public AnimalViewModel(@NonNull Application application) {
        super(application);
        repository = new AnimalRepository(application);
        allAnimals = repository.getAllAnimals();
    }

    public void insert(AnimalEntity animal, int userId) {
        repository.insert(animal, userId);
    }

    public void update(AnimalEntity animal) {
        repository.update(animal);
    }

    public void delete(AnimalEntity animal) {
        repository.delete(animal);
    }

    public LiveData<List<AnimalEntity>> getAllAnimals() {
        return allAnimals;
    }

    public LiveData<UserWithAnimals> getUserWithAnimals(int userId) {
        return repository.getUserWithAnimals(userId);
    }
}
