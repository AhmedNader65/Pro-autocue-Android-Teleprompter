package com.mrerror.free.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mrerror.proautocue_androidteleprompter.R;
import com.mrerror.proautocue_androidteleprompter.utils.BaseActivity;

public class TeleOrCam extends BaseActivity {



    String content;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_or_cam);
        LinearLayout camLayout = (LinearLayout) findViewById(R.id.cam_linear);
        LinearLayout teleLayout = (LinearLayout) findViewById(R.id.linearLayout);
        content = getIntent().getStringExtra("content");
        Log.e("connntennnnt",content);
        camLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Record");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                startActivity(new Intent(TeleOrCam.this, RecordActivity.class).putExtra("content",content));
            }
        });
        teleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Teleprompter");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                startActivity(new Intent(TeleOrCam.this, Teleprompter.class).putExtra("content",content));
            }
        });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
         }
}
