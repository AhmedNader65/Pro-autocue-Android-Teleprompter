package com.mrerror.proautocue_androidteleprompter.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Dimension;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.mrerror.proautocue_androidteleprompter.R;
import com.mrerror.proautocue_androidteleprompter.data.TeleprompterPreferences;


/**
 * Created by ahmed on 10/08/17.
 */

public class TeleUtils {
    public static int CODE_WRITE_SETTINGS_PERMISSION=77;
    public static int getSizeFromPreference(int pref){
        switch (pref){
            case 1 :
                return 24;
            case 2:
                return 28;
            case 3:
                return 32;
            case 4:
                return 36;
            case 5:
                return 40;
            default:
                return 24;
        }
    }
    public static int dpToPixel(Context context,float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }


    public static void setTextSize(Context context,TextView textView) {
        int textSize = TeleUtils.getSizeFromPreference(TeleprompterPreferences.getPreferredSize(context));
        textView.setTextSize(Dimension.SP,textSize);
    }


    public static int setScrollSpeed(Context context) {
        int speedSeekbar = TeleprompterPreferences.getPreferredSpeed(context);
        return  speedSeekbar * speedSeekbar;
    }

    public static void setTextColor(Context context,TextView textView) {
        int textColor = TeleprompterPreferences.getPreferredTextColor(context);

        if(textColor!=0)
          textView.setTextColor(textColor);
    }
    public static void setBackgroundColor(Context context,TextView textView) {
        int backgroundColor = TeleprompterPreferences.getPreferredBackgroundColor(context);
        textView.setBackgroundColor(backgroundColor);
    }
    public static void setLineHeight(Context context,TextView textView) {
        int lineSpace = TeleprompterPreferences.getPreferredSpace(context);
        textView.setLineSpacing(TeleUtils.dpToPixel(context,lineSpace*lineSpace +1 -lineSpace), 1);
    }


    public static void setMirrorMode(Context context,TextView textView) {
        boolean isMM = TeleprompterPreferences.isMirrorMode(context);
        if(isMM)
            textView.setScaleX(-1);
        else
            textView.setScaleX(1);

    }

    public static void setTimeout(Context context) {
        boolean isTimeoutDisabled = TeleprompterPreferences.isTimeoutDisabled(context);
        if(isTimeoutDisabled)
            ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



    public static void checkForWriteSettings(Activity context){
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  //this will set the manual mode (set the automatic mode off)
            android.provider.Settings.System.putInt(context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, TeleprompterPreferences.getPreferredBrightness(context));
        }  else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, CODE_WRITE_SETTINGS_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, CODE_WRITE_SETTINGS_PERMISSION);
            }
        }
    }

    public static void saveHowManyWeeks(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String lastNotificationKey = context.getString(R.string.pref_weeks_notification);
        editor.putInt(lastNotificationKey, sp.getInt(context.getString(R.string.pref_weeks_notification),0)+1);
        editor.apply();
    }

    public static int getHowManyWeeks(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int weeks = sp.getInt(context.getString(R.string.pref_weeks_notification),0);
        return weeks;
    }
}
