package com.price.samplenew;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class ResultActivity extends Activity {
    JSONObject jsonObject;
    JSONArray jsonArrayItems;
    LinearLayout lyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        String jsonString = intent.getStringExtra("jsonObject");

        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            jsonArrayItems = jsonObject1.getJSONArray("listings");
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        lyContainer = (LinearLayout)findViewById(R.id.lyContainer);
        presentData();
    }

    void presentData() {
        for(int i = 0; i < jsonArrayItems.length(); i ++) {
            try {
                final JSONObject jsonObject = jsonArrayItems.getJSONObject(i);

                LayoutInflater inflater = LayoutInflater.from(this);
                View viewResult = inflater.inflate(R.layout.cell_result, null);//Question!!

                ImageView imgvResult = (ImageView) viewResult.findViewById(R.id.imgvResult);
                TextView tvPrice = (TextView)viewResult.findViewById(R.id.tvPrice);
                TextView tvAddress = (TextView)viewResult.findViewById(R.id.tvAddress);

                String strImageURL = jsonObject.getString("img_url");
                String strPrice = jsonObject.getString("price_formatted");
                String strAddress = jsonObject.getString("title");

                tvPrice.setText(strPrice);
                tvAddress.setText(strAddress);
                new DownloadImageTask((ImageView)viewResult.findViewById(R.id.imgvResult))
                        .execute(strImageURL);

                viewResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
                        intent.putExtra("jsonObject", jsonObject.toString());
                        startActivity(intent);
                    }
                });

                lyContainer.addView(viewResult);

            } catch (JSONException e) {
                e.printStackTrace();
            }


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
