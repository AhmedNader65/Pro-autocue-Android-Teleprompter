package com.mrerror.paid.activity;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.mrerror.paid.adapters.MyScriptsAdapter;
import com.mrerror.paid.utils.ReminderUtilities;
import com.mrerror.proautocue_androidteleprompter.utils.BaseActivity;
import com.mrerror.proautocue_androidteleprompter.R;
import com.mrerror.proautocue_androidteleprompter.data.ScriptsContract.ScriptEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ScriptsActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ID_SCRIPTS_LOADER = 7;


    RecyclerView mRecyclerView;
    private FilePickerDialog dialog;
    private MyScriptsAdapter mAdapter;
    int mPosition = RecyclerView.NO_POSITION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_scripts);
        mAdapter = new MyScriptsAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooser();
            }
        });


        ReminderUtilities.scheduleChargingReminder(this);
        getSupportLoaderManager().initLoader(ID_SCRIPTS_LOADER, null, this);
    }

    private void showChooser() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.STORAGE_DIR);
        properties.error_dir = new File(DialogConfigs.DIRECTORY_SEPERATOR);
        properties.offset = new File(DialogConfigs.DIRECTORY_SEPERATOR);
        properties.extensions = new String[]{".txt"};
        dialog = new FilePickerDialog(ScriptsActivity.this,properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                for(String path:files) {
                    File file=new File(path);
                    Log.d("file name ",file.getName());
                    Log.d("file path ",file.getAbsolutePath());
                    try {
                        ContentValues values = new ContentValues();
                        values.put(ScriptEntry.SCRIPT_PATH,path);
                        Uri insertUri =  getContentResolver().insert(ScriptEntry.CONTENT_URI,values);
                        Log.e("insert uri >> ", insertUri.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        dialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(dialog!=null)
                    {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }
                }
                else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(ScriptsActivity.this,"Permission is Required for getting list of files",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {
            case ID_SCRIPTS_LOADER: {
                Uri scriptsQueryUri = ScriptEntry.CONTENT_URI;
                String sortOrder = ScriptEntry._ID + " DESC";

                return new CursorLoader(this,
                        scriptsQueryUri,
                        null,
                        null,
                        null,
                        sortOrder);


            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_clear:
                getContentResolver().delete(ScriptEntry.CONTENT_URI,null,null);
                getSupportLoaderManager().restartLoader(ID_SCRIPTS_LOADER, null, this);
                break;
        }
        return true;
    }
}
