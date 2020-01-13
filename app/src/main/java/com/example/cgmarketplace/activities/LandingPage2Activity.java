package com.example.cgmarketplace.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cgmarketplace.R;

public class LandingPage2Activity extends AppCompatActivity {

    Button btn_skip, btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page2);

        getSupportActionBar().hide();

        btn_next =findViewById(R.id.btn_next);
        btn_skip=findViewById(R.id.btn_skip);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonextlandingpage = new Intent(LandingPage2Activity.this,LandingPage3Activity.class);
                startActivity(gotonextlandingpage);
                finish();
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonextlandingpage = new Intent(LandingPage2Activity.this,LandingPage3Activity.class);
                startActivity(gotonextlandingpage);
                finish();
            }
        });
    }
}
