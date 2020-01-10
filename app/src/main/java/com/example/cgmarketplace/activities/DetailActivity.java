package com.example.cgmarketplace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.cgmarketplace.R;

import me.relex.circleindicator.CircleIndicator3;

public class DetailActivity extends AppCompatActivity {
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


    }
}
