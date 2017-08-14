package com.mrerror.paid.widget;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mrerror.paid.adapters.MyScriptsWidgetAdapter;
import com.mrerror.proautocue_androidteleprompter.R;
import com.mrerror.proautocue_androidteleprompter.data.ScriptsContract;

/**
 * The configuration screen for the {@link ScriptAppWidget ScriptAppWidget} AppWidget.
 */
public class ScriptAppWidgetConfigureActivity extends Activity implements  LoaderCallbacks<Cursor>,MyScriptsWidgetAdapter.OnSelectScript {
    private static final int ID_SCRIPTS_LOADER = 7;

    private static final String PREFS_NAME = "com.mrerror.free.widget.ScriptAppWidget";
    public static final String PREF_PREFIX_KEY = "appwidget_";
    public static final String PREF_CONTENT_KEY = "appwidget_content";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    MyScriptsWidgetAdapter adapter;
    int mPosition = RecyclerView.NO_POSITION;
/*
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = ScriptAppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
//            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ScriptAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };*/
    private RecyclerView recycler;

    public ScriptAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }
    // Write the content to the SharedPreferences object for this widget
    static void saveContentPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_CONTENT_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId,String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(key + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.script_app_widget_configure);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler = (RecyclerView) findViewById(R.id.list);
//        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
        recycler.setLayoutManager(layoutManager);
        adapter = new MyScriptsWidgetAdapter(this);
        recycler.setAdapter(adapter);
        getLoaderManager().initLoader(ID_SCRIPTS_LOADER, null, this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }

    @Override
    public Loader<Cursor>  onCreateLoader(int id, Bundle args) {

        Log.e("widgeeet","first loading");
        switch (id) {
            case ID_SCRIPTS_LOADER: {
                Log.e("widgeeet","second loading");
                Uri scriptsQueryUri = ScriptsContract.ScriptEntry.CONTENT_URI;
                Log.e("widgeeet","third loading"+ scriptsQueryUri.toString());
                String sortOrder = ScriptsContract.ScriptEntry._ID + " DESC";
                return new CursorLoader(this,
                        scriptsQueryUri,
                        null,
                        null,
                        null,
                        sortOrder);


            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        recycler.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }



    public void onClick(String title,String content) {
        final Context context = ScriptAppWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
            saveTitlePref(context, mAppWidgetId, title);
            saveContentPref(context, mAppWidgetId, content);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ScriptAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

