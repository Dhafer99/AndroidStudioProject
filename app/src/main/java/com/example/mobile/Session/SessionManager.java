package com.example.mobile.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";

    private static final String KEY_USER_ROLE = "role";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save login state
    public void createLoginSession(int userId,String role) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Get the logged-in user ID
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "USER");
    }

    // Logout user
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
