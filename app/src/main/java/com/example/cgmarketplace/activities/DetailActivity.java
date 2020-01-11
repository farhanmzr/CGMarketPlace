package com.example.cgmarketplace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cgmarketplace.R;

import me.relex.circleindicator.CircleIndicator3;

public class DetailActivity extends AppCompatActivity {
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";

    ImageView btn_back;
    TextView tv_nama, tv_price, tv_desc, tv_width, tv_height, tv_dense, tv_finishing, tv_material, tv_details_1, tv_details_2, tv_details_3, tv_details_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btn_back = findViewById(R.id.btn_back);
        tv_nama = findViewById(R.id.tv_nama);
        tv_price = findViewById(R.id.tv_price);
        tv_desc = findViewById(R.id.tv_desc);
        tv_width = findViewById(R.id.tv_width);
        tv_height = findViewById(R.id.tv_height);
        tv_dense = findViewById(R.id.tv_dense);
        tv_finishing = findViewById(R.id.tv_finishing);
        tv_material = findViewById(R.id.tv_material);
        tv_details_1 = findViewById(R.id.tv_details_1);
        tv_details_2 = findViewById(R.id.tv_details_2);
        tv_details_3 = findViewById(R.id.tv_details_3);
        tv_details_4 = findViewById(R.id.tv_details_4);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtohome = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(backtohome);
                finish();
                overridePendingTransition(0,0);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
        });


    }
}
