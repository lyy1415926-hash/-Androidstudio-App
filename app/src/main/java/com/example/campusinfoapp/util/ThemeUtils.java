package com.example.campusinfoapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_IS_NIGHT = "is_night";

    public static void applyTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isNight = prefs.getBoolean(KEY_IS_NIGHT, false);
        AppCompatDelegate.setDefaultNightMode(isNight ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static void toggleTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isNight = prefs.getBoolean(KEY_IS_NIGHT, false);
        prefs.edit().putBoolean(KEY_IS_NIGHT, !isNight).apply();
        applyTheme(context);
    }
}
