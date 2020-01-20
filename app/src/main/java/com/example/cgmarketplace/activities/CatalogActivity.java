package com.example.cgmarketplace.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cgmarketplace.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CatalogActivity extends AppCompatActivity {

    private LinearLayout sort_by;

    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private String userId;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.transaction_title);

        sort_by = findViewById(R.id.sort_by);


        alertDialog();

        sort_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
                alertDialog.show();
            }
        });

    }



    private void alertDialog() {
        String[] items = {"A to Z", "Z to A", "Low Price to High", "High Price to Low"};
        builder = new AlertDialog.Builder(CatalogActivity.this);
        builder.setTitle(R.string.sort_by);

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialog = builder.create();
    }
}
