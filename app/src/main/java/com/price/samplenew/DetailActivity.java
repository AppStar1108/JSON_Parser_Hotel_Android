package com.price.samplenew;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class DetailActivity extends Activity {

    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        String jsonString = intent.getStringExtra("jsonObject");

        try {
            jsonObject = new JSONObject(jsonString);

            String strUrl = jsonObject.getString("img_url");
            String strPrice = jsonObject.getString("price_formatted");
            String strAddress = jsonObject.getString("title");
            String strStructure = jsonObject.getString("keywords");
            String strDetail = jsonObject.getString("summary");

            ((TextView)findViewById(R.id.tvPrice)).setText(strPrice);
            ((TextView)findViewById(R.id.tvAddress)).setText(strAddress);
            ((TextView)findViewById(R.id.tvStructure)).setText(strStructure);
            ((TextView)findViewById(R.id.tvDetail)).setText(strDetail);
            new DownloadImageTask((ImageView) findViewById(R.id.imgvContent))
                    .execute(strUrl);

        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
