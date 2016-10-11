package com.example.applicationb;


import android.app.Activity;
import android.app.Notification;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.UserDictionary;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class SaveActivity extends Activity {

    ImageView imageView;
    final Uri CONTACT_URI = Uri.parse("content://com.application.provider.LinkProvider/links");
    long id;
    int status;
    ImageTask imageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        id = intent.getLongExtra("id", 0);
        status = intent.getIntExtra("status", 3);

        imageTask = new ImageTask(imageView);
        imageTask.execute(link);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 2 && hasImage(imageView) || status == 3 && hasImage(imageView)){
                    ContentValues cv = new ContentValues();
                    cv.put("status", 1);
                    Uri uri = ContentUris.withAppendedId(CONTACT_URI, id);
                    getContentResolver().update(uri, cv, null, null);
                }
                else if (status == 1 && !hasImage(imageView)){
                    ContentValues cv = new ContentValues();
                    cv.put("status", 2);
                    Uri uri = ContentUris.withAppendedId(CONTACT_URI, id);
                    getContentResolver().update(uri, cv, null, null);
                }
                else if (imageTask.success && hasImage(imageView)) {
                    saveImage(imageTask.mIcon11);
                    Uri uri = ContentUris.withAppendedId(CONTACT_URI, id);
                    int cnt = getContentResolver().delete(uri, null, null);
                    Log.d("TAG", "delete, count = " + cnt);
                    notific();
                }
                else {
                    toast();
                }
            }
        }, 15000);

    }

    public void notific() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle("Notification")
                .setContentText("Link is deleted");

        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(101, notification);

    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/sdcard/Bonsite/test/B");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()){
            file.delete ();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            Log.d("TAG", "SUCCESS");
            out.close();

        } catch (Exception e) {
            Log.d("TAG", "FAIL");
            e.printStackTrace();
        }
    }

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public void toast(){
        Toast.makeText(this, "Link with error", Toast.LENGTH_LONG).show();
    }

}
