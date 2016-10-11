package com.example.applicationb;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageTask extends AsyncTask<String, Void, Bitmap>{

    ImageView bmImage;
    Bitmap mIcon11;
    boolean success = false;


    public ImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];

        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            this.success = true;
            in.close();
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        Log.d("MYTAG", String.valueOf(success));
        bmImage.setImageBitmap(result);
    }


}
