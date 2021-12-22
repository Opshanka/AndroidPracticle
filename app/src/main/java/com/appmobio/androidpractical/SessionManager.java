package com.appmobio.androidpractical;

import android.content.Context;
import android.content.SharedPreferences;

class SessionManager {
    //initialize varibales
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //Create Constructor
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    //Create set  login method
    public void setLogin(boolean login) {
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    //Create get login method
    public boolean getLogin() {
        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    //Create set email
    public void set_email(String email) {
        editor.putString("KEY_email", email);
        editor.commit();
    }

    //Create get login email
    public String get_email() {
        return sharedPreferences.getString("KEY_email", "");
    }


    //Create set user_id
    public void set_userId(String user_id) {
        editor.putString("KEY_USER_ID", user_id);
        editor.commit();
    }

    //Create get login userId
    public String get_userId() {
        return sharedPreferences.getString("KEY_USER_ID", "");
    }

    //Create set first_name
    public void set_first_name(String first_name) {
        editor.putString("KEY_FIRST_NAME", first_name);
        editor.commit();
    }

    //Create get login _first_name
    public String get_first_name() {
        return sharedPreferences.getString("KEY_FIRST_NAME", "");
    }
}
