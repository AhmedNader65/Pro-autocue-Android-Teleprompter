package com.mrerror.free.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mrerror.proautocue_androidteleprompter.R;
import com.mrerror.proautocue_androidteleprompter.data.TeleprompterPreferences;
import com.mrerror.proautocue_androidteleprompter.settings.SettingsActivity;
import com.mrerror.proautocue_androidteleprompter.utils.BaseActivity;
import com.mrerror.proautocue_androidteleprompter.utils.OnSwipeListener;
import com.mrerror.proautocue_androidteleprompter.utils.TeleUtils;

import static com.mrerror.proautocue_androidteleprompter.utils.TeleUtils.checkForWriteSettings;


public class Teleprompter extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener
,View.OnTouchListener{


    private ScrollView mScrollView;
    private TextView textView;
    private int mScrollBy;
    boolean isPlaying = false;

    private GestureDetector gestureDetector;
    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    private String DEBUG_TAG = "TELEPROMPTER ACTIVITY";
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teleprompter);


        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
            }
        });
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setBackgroundColor(getColor(R.color.telePrimaryColor));
            getWindow().setStatusBarColor(getColor(R.color.telePrimaryColorDark));
        }else{
            toolbar.setBackgroundColor(getResources().getColor(R.color.telePrimaryColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.telePrimaryColorDark));
        }

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        sp = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        textView = (TextView) findViewById(R.id.textView);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);

        textView.setText(getIntent().getStringExtra("content"));
        final Timer timer = new Timer(10000000, 15);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_light)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPlaying){
                    timer.start();
                    mScrollView.setOnTouchListener(Teleprompter.this);
                    isPlaying = true;
                    fab.setImageResource(R.drawable.ic_pause);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light)));
                    toolbar.setVisibility(View.GONE);
                }else{
                    timer.cancel();
                    isPlaying = false;
                    mScrollView.setOnTouchListener(null);
                    fab.setImageResource(R.drawable.ic_play);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_light)));
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });
        TeleUtils.setTextSize(this,textView);
        mScrollBy = TeleUtils.setScrollSpeed(this);

        TeleUtils.setLineHeight(this,textView);
        TeleUtils.setTextColor(this,textView);
        TeleUtils.setBackgroundColor(this,textView);
        TeleUtils.setMirrorMode(this,textView);
        TeleUtils.setTimeout(this);
        gestureDetector=new GestureDetector(this,new OnSwipeListener(){

            @Override
            public boolean onSwipe(Direction direction) {
                if (direction==Direction.up){
                    if(TeleprompterPreferences.getPreferredSpeed(Teleprompter.this)<5) {
                        editor.putInt(getString(R.string.speed_key),
                                TeleprompterPreferences.getPreferredSpeed(Teleprompter.this) +1);
                        editor.commit();
                    }
                    //do your stuff
                    Log.d(DEBUG_TAG, "onSwipe: up");


                }

                if (direction==Direction.down){
                    if(TeleprompterPreferences.getPreferredSpeed(Teleprompter.this)>1) {
                        editor.putInt(getString(R.string.speed_key),
                                TeleprompterPreferences.getPreferredSpeed(Teleprompter.this) - 1);
                        editor.commit();
                    }
                    //do your stuff
                    Log.d(DEBUG_TAG, "onSwipe: down");
                }

                if (direction==Direction.left){
                    //do your stuff
                    if(TeleprompterPreferences.getPreferredSize(Teleprompter.this)>1) {
                        editor.putInt(getString(R.string.size_key),
                                TeleprompterPreferences.getPreferredSize(Teleprompter.this) - 1);
                        editor.commit();
                    }
                    Log.d(DEBUG_TAG, "onSwipe: left");
                }

                if (direction==Direction.right){

                    if(TeleprompterPreferences.getPreferredSize(Teleprompter.this)<5) {
                        //do your stuff
                        editor.putInt(getString(R.string.size_key),
                                TeleprompterPreferences.getPreferredSize(Teleprompter.this) + 1);
                        editor.commit();
                    }
                    Log.d(DEBUG_TAG, "onSwipe: right");
                }
                return true;
            }


        });
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e("changeddd", ">>>>>>>>>>>>>>><<<<<<<<<<<<<<<<");
        if(key.equals(getString(R.string.speed_key))) {
            mScrollBy = TeleUtils.setScrollSpeed(this);
        }
        if(key.equals(getString(R.string.size_key))){
            TeleUtils.setTextSize(this,textView);
        }
        if(key.equals(getString(R.string.space_key))){
            TeleUtils.setLineHeight(this,textView);
        }
        if(key.equals(getString(R.string.text_color_key))){
            TeleUtils.setTextColor(this,textView);
        }
        if(key.equals(getString(R.string.background_color_key))){
            TeleUtils.setBackgroundColor(this,textView);
        }
        if(key.equals(getString(R.string.mirror_key))){
            TeleUtils.setMirrorMode(this,textView);
        }
        if(key.equals(getString(R.string.timeout_key))){
            TeleUtils.setTimeout(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(DEBUG_TAG, "onTouch: ");
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public class Timer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            mScrollView.smoothScrollBy(0, mScrollBy);
        }

        @Override
        public void onFinish() {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scripts,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this,SettingsActivity.class));
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (TeleprompterPreferences.isManualBrightnessEnabled(this)) {
            checkForWriteSettings(this);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);  //this will set the manual mode (set the automatic mode off)
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TeleprompterPreferences.isManualBrightnessEnabled(this))
            checkForWriteSettings(this);
    }

}
