package com.application.closed.testapp;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

class MyAdapter extends CursorAdapter{

    MyAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.views, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvLink = (TextView) view.findViewById(R.id.tvLink);
        String link = cursor.getString(cursor.getColumnIndexOrThrow("ref"));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
        tvLink.setText(link);

        if (status == 1){
            tvLink.setBackgroundColor(Color.GREEN);
        } else if (status == 2){
            tvLink.setBackgroundColor(Color.RED);
        } else if (status == 3){
            tvLink.setBackgroundColor(Color.GRAY);
        }
        notifyDataSetChanged();
    }
}
