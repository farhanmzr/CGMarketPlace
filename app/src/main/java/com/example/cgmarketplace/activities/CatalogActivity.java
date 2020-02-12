package com.example.cgmarketplace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.ProductAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class CatalogActivity extends AppCompatActivity implements ProductAdapter.OnProductSelectedListener {
    private static final String TAG = "CatalogActivity";
    public static final String KEY_PRODUCT_CATEGORY = "key_product_category";
    public static final String KEY_SEARCH_QUERY = "key_search_query";



    private View view;
    private TextView tvSortby;
    private Button btn_cancel, btn_confirm;

    private RecyclerView catalog_recyclerview;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private ProductAdapter mAdapter;
    private String productCategory, searchQuery ;
    private ImageView imgEmptySearch;
    private TextView tvTitle, textEmptySearch, textEmptySearch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productCategory = getIntent().getExtras().getString(KEY_PRODUCT_CATEGORY);
        searchQuery = getIntent().getExtras().getString(KEY_SEARCH_QUERY);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(productCategory);
        textEmptySearch = findViewById(R.id.textEmptySearch);
        textEmptySearch2 = findViewById(R.id.textEmptySearch2);
        imgEmptySearch = findViewById(R.id.imageEmptySearch);


        catalog_recyclerview = findViewById(R.id.category_recyclerview);

        initFirestore();
        initRecyclerView();



    }

    private void initFirestore() {

        mFirestore = FirebaseFirestore.getInstance();

        if (productCategory.equals("All")) {

            mQuery = mFirestore.collection("Produk");
            getSupportActionBar().setTitle("All Category");

        }
        if (productCategory.equals("Search Result")) {
            mQuery = mFirestore.collection("Produk");
            mQuery = mQuery.whereEqualTo("name", searchQuery);
            Log.w("Query", searchQuery);
            getSupportActionBar().setTitle(productCategory);

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
                    textEmptySearch.setVisibility(View.VISIBLE);
                    textEmptySearch2.setVisibility(View.VISIBLE);
                    imgEmptySearch.setVisibility(View.VISIBLE);

                    Log.w(TAG, "ItemCount = 0");
                } else {
                    catalog_recyclerview.setVisibility(View.VISIBLE);
                    textEmptySearch.setVisibility(View.GONE);
                    textEmptySearch2.setVisibility(View.GONE);
                    imgEmptySearch.setVisibility(View.GONE);


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
