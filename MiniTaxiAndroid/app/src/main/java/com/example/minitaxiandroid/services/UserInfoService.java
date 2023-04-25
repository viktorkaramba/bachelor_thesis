package com.example.minitaxiandroid.services;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.minitaxiandroid.constants.InfoStorageConstants;

public class UserInfoService {
    public static String STORAGE_NAME = InfoStorageConstants.USER_STORAGE;
    private static SharedPreferences settings = null;
    private static SharedPreferences.Editor editor = null;
    private static Context _context = null;

    public static void init(Context context){
        _context = context;
    }

    private static void init(){
        settings = _context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public static void addProperty(String name, String value){
        if(settings == null){
            init();
        }
        editor.putString(name, value);
        editor.apply();
    }

    public static String getProperty(String name){
        if(settings == null){
            init();
        }
        return settings.getString(name, null);
    }

    public static void clear(){
        if(settings == null){
            init();
        }
        editor.clear();
        editor.apply();
    }
}
