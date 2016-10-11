package com.example.applicationb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImageActivity extends AppCompatActivity {


    ImageView imageView;
    ImageTask imageTask;
    String link;
    SimpleDateFormat dateFormat;
    Date date;

    static final String LINK_STATUS = "status";
    static final String LINK_REF = "ref";
    static final String LINK_TIME = "time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        final Uri CONTACT_URI = Uri.parse("content://com.application.provider.LinkProvider/links");

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date();


        Intent intent = getIntent();
        link = intent.getStringExtra("link");


        imageView = (ImageView) findViewById(R.id.imageView);
        imageTask = new ImageTask(imageView);
        imageTask.execute(link);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasImage(imageView)){
                    ContentValues cv = new ContentValues();
                    cv.put(LINK_REF, link);
                    cv.put(LINK_TIME, dateFormat.format(date));
                    cv.put(LINK_STATUS, 1);
                    getContentResolver().insert(CONTACT_URI, cv);
                }
                else if (!imageTask.success) {
                    ContentValues cv = new ContentValues();
                    cv.put(LINK_REF, link);
                    cv.put(LINK_TIME, dateFormat.format(date));
                    cv.put(LINK_STATUS, 2);
                    getContentResolver().insert(CONTACT_URI, cv);
                }

                else {
                    ContentValues cv = new ContentValues();
                    cv.put(LINK_REF, link);
                    cv.put(LINK_TIME, dateFormat.format(date));
                    cv.put(LINK_STATUS, 3);
                    getContentResolver().insert(CONTACT_URI, cv);
                }
            }
        }, 10000);

    }

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }


}
