package com.example.mobile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.mobile.DAO.PlanFoodDao;
import com.example.mobile.database.DatabaseProvider;
import com.example.mobile.database.FoodEntity;
import com.example.mobile.database.PetCareDatabase;
import com.example.mobile.database.PlanFoodEntity;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.relations.PlanFoodCrossRef;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetCareDatabase db = DatabaseProvider.getDatabase(this);
        PlanFoodDao planFoodDao = db.planFoodDao();
        // Access DAOs to perform database operations
        new Thread(() -> {
            // Example: Insert a user
            UserEntity user = new UserEntity();
            user.setName("John Doe");
            user.setEmail("john@example.com");
            user.setPhoneNumber("123456789");
            user.setPassword("123");
            db.userDao().insertUser(user);

            // Example: Fetch all users
            List<UserEntity> users = db.userDao().getAllUsers();
            for (UserEntity u : users) {
                System.out.println(u.getName());
            }


            // exemple insert plan with food
            FoodEntity food = new FoodEntity();
            food.setNom("Pomme");
            food.setDescription("Une pomme fraîche");
            food.setImage("url_image_pomme");
            food.setType("Fruit");
            food.setCategorie("Fruits");
            food.setQuantite(1);
            food.setUnite("Pièce");

            long foodId = planFoodDao.insertFood(food);

            // Création d'un objet PlanFood
            PlanFoodEntity plan = new PlanFoodEntity();
            plan.setJour("Lundi");
            plan.setType("Petit déjeuner");

            long planId = planFoodDao.insertPlan(plan);

            // Création d'un objet PlanFoodCrossRef pour lier food et plan
            PlanFoodCrossRef crossRef = new PlanFoodCrossRef();
            crossRef.setFoodId(foodId);
            crossRef.setPlanId(planId);
            planFoodDao.insertPlanFoodCrossRef(crossRef);

            Log.d("Insertion", "Insertion réussie avec FoodEntity ID: " + foodId + " et PlanFood ID: " + planId);

        }).start();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_food, R.id.nav_login, R.id.nav_signup, R.id.nav_service_fares)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}