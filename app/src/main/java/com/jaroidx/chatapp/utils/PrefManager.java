package com.jaroidx.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static SharedPreferences mSharedPreferences;

    public static void saveBoolean(Context context, String key, boolean data) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putBoolean(key, data).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, false);
    }

    public static void saveString(Context context, String key, String data) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putString(key, data).commit();
    }

    public static String getString(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, null);
    }

    public static void saveInteger(Context context, String key, int data) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putInt(key, data).commit();
    }

    public static int getInteger(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, -1);
    }

    public static void saveFloat(Context context, String key, float data) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putFloat(key, data).commit();
    }

    public static float getFloat(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(key, -1);
    }

    public static void saveLong(Context context, String key, long data) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putLong(key, data).commit();
    }

    public static long getLong(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences(PrefKey.PREF_FILE, Context.MODE_PRIVATE);
        return mSharedPreferences.getLong(key, -1);
    }

    public class PrefKey{
        public static final String DATA_LOGIN = "DATA_LOGIN";
        public static final String PREF_FILE = "chat_app";
        public static final String IS_LOGIN = "IS_LOGIN";
    }
}
