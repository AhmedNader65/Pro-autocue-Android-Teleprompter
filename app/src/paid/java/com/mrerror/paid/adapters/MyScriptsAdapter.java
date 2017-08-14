package com.mrerror.paid.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrerror.paid.activity.TeleOrCam;
import com.mrerror.proautocue_androidteleprompter.R;
import com.mrerror.proautocue_androidteleprompter.data.ScriptsContract;

import java.io.File;

import static com.mrerror.paid.activity.ScriptsActivity.getStringFromFile;


/**
 * Created by ahmed on 08/08/17.
 */

public class MyScriptsAdapter extends RecyclerView.Adapter<MyScriptsAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public MyScriptsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.script_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String scriptPath = mCursor.getString(mCursor.getColumnIndex(ScriptsContract.ScriptEntry.SCRIPT_PATH));
        File file=new File(scriptPath);
        holder.titleTV.setText(file.getName());
        try {
            holder.contentTV.setText(getStringFromFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView contentTV ;
        TextView titleTV ;

        public ViewHolder(View itemView) {
            super(itemView);
            contentTV = (TextView) itemView.findViewById(R.id.script_content);
            titleTV = (TextView) itemView.findViewById(R.id.script_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mContext.startActivity(new Intent(mContext, TeleOrCam.class).putExtra("content",contentTV.getText().toString()));
        }
    }
}
