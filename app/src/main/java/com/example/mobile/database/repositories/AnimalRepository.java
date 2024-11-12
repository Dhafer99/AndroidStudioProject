package com.example.mobile.database.repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.mobile.DAO.AnimalDao;
import com.example.mobile.database.AnimalEntity;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.relations.UserWithAnimals;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimalRepository {

    private final AnimalDao animalDao;
    private final LiveData<List<AnimalEntity>> allAnimalsLiveData;
    private final ExecutorService executorService;

    public AnimalRepository(Context context) {
        PetCareDatabase db = DatabaseProvider.getDatabase(context);
        animalDao = db.animalDao();
        allAnimalsLiveData = animalDao.getAllAnimals();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(AnimalEntity animal, int ownerId) {
        animal.setOwnerId(ownerId);
        executorService.execute(() -> animalDao.insertAnimal(animal));
    }

    public void update(AnimalEntity animal) {
        executorService.execute(() -> animalDao.updateAnimal(animal));
    }

    public void delete(AnimalEntity animal) {
        executorService.execute(() -> animalDao.deleteAnimal(animal));
    }

    public LiveData<List<AnimalEntity>> getAllAnimals() {
        return allAnimalsLiveData;
    }

    public LiveData<UserWithAnimals> getUserWithAnimals(int userId) {
        MutableLiveData<UserWithAnimals> liveData = new MutableLiveData<>();
        executorService.execute(() -> {
            UserWithAnimals userWithAnimals = animalDao.getUserWithAnimals(userId);
            liveData.postValue(userWithAnimals);
        });
        return liveData;
    }
}
