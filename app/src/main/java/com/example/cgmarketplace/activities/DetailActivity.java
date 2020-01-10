package com.example.cgmarketplace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cgmarketplace.R;

import me.relex.circleindicator.CircleIndicator3;

public class DetailActivity extends AppCompatActivity {

    ImageView ic_back;
    TextView tv_namabarang, tv_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ic_back = findViewById(R.id.ic_back);
        tv_namabarang = findViewById(R.id.tv_namabarang);
        tv_price = findViewById(R.id.tv_price);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
