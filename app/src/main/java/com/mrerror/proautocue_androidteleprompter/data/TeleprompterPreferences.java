package com.mrerror.proautocue_androidteleprompter.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.mrerror.proautocue_androidteleprompter.R;

/**
 * Created by ahmed on 10/08/17.
 */

public class TeleprompterPreferences {

    public static int getPreferredSpeed(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForSpeed = context.getString(R.string.speed_key);
        String defaultSpeed = context.getString(R.string.speed_default);
        return prefs.getInt(keyForSpeed, Integer.parseInt(defaultSpeed));
    }
    public static int getPreferredSize(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForSize = context.getString(R.string.size_key);
        String defaultSize = context.getString(R.string.size_default);
        return prefs.getInt(keyForSize, Integer.parseInt(defaultSize));
    }
    public static int getPreferredSpace(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForSpace = context.getString(R.string.space_key);
        String defaultSpace = context.getString(R.string.space_default);
        return prefs.getInt(keyForSpace, Integer.parseInt(defaultSpace));
    }
    public static int getPreferredTextColor(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForColor = context.getString(R.string.text_color_key);
        int defaultColor = 0;
        return prefs.getInt(keyForColor, defaultColor);
    }
    public static int getPreferredBackgroundColor(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForColor = context.getString(R.string.background_color_key);
        int defaultColor = 0x000;
        return prefs.getInt(keyForColor, defaultColor);
    }

    public static boolean isMirrorMode(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForMM = context.getString(R.string.mirror_key);
        boolean defaultMM = false;
        return prefs.getBoolean(keyForMM, defaultMM);
    }
    public static boolean isTimeoutDisabled(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String timeoutDisableKey = context.getString(R.string.timeout_key);
        boolean defaultTimeout = true;
        return prefs.getBoolean(timeoutDisableKey, defaultTimeout);
    }


    public static boolean isManualBrightnessEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String manualBrKey = context.getString(R.string.manual_brightness_key);
        boolean defaultManualBR = false;
        return prefs.getBoolean(manualBrKey, defaultManualBR);
    }

    public static int getPreferredBrightness(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForBright = context.getString(R.string.brightness_key);
        String defaultBright = context.getString(R.string.brightness_default);
        return prefs.getInt(keyForBright, Integer.parseInt(defaultBright));
    }

}
