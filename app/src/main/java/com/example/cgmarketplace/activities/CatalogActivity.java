package com.example.cgmarketplace.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.ProductAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class CatalogActivity extends AppCompatActivity implements ProductAdapter.OnProductSelectedListener {
    private static final String TAG = "CatalogActivity";
    public static final String KEY_PRODUCT_CATEGORY = "key_product_category";

    private LinearLayout sort_by;

    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    private RecyclerView catalog_recyclerview;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private ProductAdapter mAdapter;
    private String productCategory;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productCategory = getIntent().getExtras().getString(KEY_PRODUCT_CATEGORY);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(productCategory);

        sort_by = findViewById(R.id.sort_by);
        catalog_recyclerview = findViewById(R.id.category_recyclerview);

        initFirestore();
        initRecyclerView();
        alertDialog();

        sort_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
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

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        alertDialog = builder.create();
    }

    private void initFirestore() {

        mFirestore = FirebaseFirestore.getInstance();

        if (productCategory.equals("All")) {

            mQuery = mFirestore.collection("Produk");
            getSupportActionBar().setTitle("All Category");

        } else {

            mQuery = mFirestore.collection("Produk");
            mQuery = mQuery.whereEqualTo("category", productCategory);
            getSupportActionBar().setTitle(productCategory);
        }
    }

    private void initRecyclerView() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new ProductAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    catalog_recyclerview.setVisibility(View.GONE);

                    Log.w(TAG, "ItemCount = 0");
                } else {
                    catalog_recyclerview.setVisibility(View.VISIBLE);

                    Log.w(TAG, "Show Produk");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        catalog_recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        catalog_recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onProductSelected(DocumentSnapshot productModel) {

        // Go to the details page for the selected restaurant
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_PRODUCT_ID, productModel.getId());

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
