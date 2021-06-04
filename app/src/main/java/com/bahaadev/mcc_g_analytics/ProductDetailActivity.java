package com.bahaadev.mcc_g_analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    AppAnalytics appAnalytics;
    final int[] timer = {0};
    CountDownTimer countDownTimer;
    String name;

    ImageView ivImage;
    TextView tvTitle,tvDetailsInfo,tvCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivImage = findViewById(R.id.ivImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvDetailsInfo = findViewById(R.id.tvDetailsInfo);
        tvCategory = findViewById(R.id.tvCategory);




        Intent myIntent = getIntent();
        String imageUrl = myIntent.getStringExtra("imageUrl");
         name = myIntent.getStringExtra("Name");
        String catName = myIntent.getStringExtra("CatName");
        String details = myIntent.getStringExtra("Details");

        details = details.substring(1, details.length()-1);
        String[] detailsVal = details.split(",");

        String FullDetails;

        for(String pair : detailsVal)
        {
            String[] entry = pair.split("=");
            FullDetails = tvDetailsInfo.getText().toString();

            tvDetailsInfo.setText(FullDetails + entry[0].trim() + " : " + entry[1].trim() + "\n\n");

        }


        Picasso.get().load(imageUrl).into(ivImage);

        tvCategory.setText("Category : " + catName);
        tvTitle.setText(name);

        appAnalytics = new AppAnalytics(getApplicationContext());
        appAnalytics.trackScreen("Product : "+ name,"ProductDetailActivity");

        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer[0]++;
            }

            @Override
            public void onFinish() {
                timer[0] =0;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        appAnalytics.UserSpends("Product : "+ name,"ProductDetailActivity",String.valueOf(timer[0]));
        countDownTimer.onFinish();
    }
}