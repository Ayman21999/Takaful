package com.graduatio.project.takaful.DataBase;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public SessionManager(Context context) {
        this.context = context;
        usersession = context.getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        editor = usersession.edit();
    }

    public void CreateSession(String email, String password) {

        editor.putBoolean(IS_LOGIN, true);



        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();

    }

    public HashMap<String, String> getUserDetailsSession() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_ID, usersession.getString(KEY_ID, null));
        userData.put(KEY_USERNAME, usersession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, usersession.getString(KEY_EMAIL, null));
        userData.put(KEY_PASSWORD, usersession.getString(KEY_PASSWORD, null));
        return userData;
    }

    public boolean CheckLogin() {
        if (usersession.getBoolean(IS_LOGIN, false)) {
            return true;

        } else {
            return false;
        }
    }

    public void LogOut() {
        editor.clear();
        editor.commit();
    }
}
