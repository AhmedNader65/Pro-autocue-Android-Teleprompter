package com.mrerror.free.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.mrerror.free.activity.TeleOrCam;
import com.mrerror.proautocue_androidteleprompter.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ScriptAppWidgetConfigureActivity ScriptAppWidgetConfigureActivity}
 */
public class ScriptAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        String content = ScriptAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId, ScriptAppWidgetConfigureActivity.PREF_CONTENT_KEY);
        Intent intent = new Intent(context, TeleOrCam.class).putExtra("content",content);
        Log.e("widgeeeet content",content );
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        CharSequence widgetText = ScriptAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId, ScriptAppWidgetConfigureActivity.PREF_PREFIX_KEY);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.script_app_widget);

        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

