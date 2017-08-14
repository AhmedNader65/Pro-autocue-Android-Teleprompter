package com.mrerror.paid.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrerror.proautocue_androidteleprompter.data.ScriptsContract;

import java.io.File;

import static com.mrerror.paid.activity.ScriptsActivity.getStringFromFile;


/**
 * Created by ahmed on 08/08/17.
 */

public class MyScriptsWidgetAdapter extends RecyclerView.Adapter<MyScriptsWidgetAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    OnSelectScript mListener;
    public MyScriptsWidgetAdapter(OnSelectScript mListener) {
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String scriptPath = mCursor.getString(mCursor.getColumnIndex(ScriptsContract.ScriptEntry.SCRIPT_PATH));
        final File file=new File(scriptPath);
        holder.titleTV.setText(file.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mListener.onClick(file.getName(),getStringFromFile(file));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView titleTV ;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTV = (TextView) itemView.findViewById(android.R.id.text1);
        }

    }


    public interface OnSelectScript {
        // TODO: Update argument type and name
        void onClick(String title, String content);
    }

}
