package com.example.cgmarketplace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cgmarketplace.R;

public class SuccessUploadPaymentActivity extends AppCompatActivity {

    private Button gotodashboard;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_upload_payment);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);


        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.success_payment);

        gotodashboard = findViewById(R.id.gotodashboard);
        gotodashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodashboard = new Intent(SuccessUploadPaymentActivity.this, TransactionActivity.class);
                gotodashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(gotodashboard);
                finish();
            }
        });
    }

}
