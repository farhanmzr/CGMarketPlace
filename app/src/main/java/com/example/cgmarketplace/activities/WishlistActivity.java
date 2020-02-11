package com.example.cgmarketplace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.WishlistAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import static com.example.cgmarketplace.R.id;
import static com.example.cgmarketplace.R.string;

public class WishlistActivity extends AppCompatActivity implements WishlistAdapter.OnProductSelectedListener {
    private final String TAG = "Wishlist Activity";

    private TextView tvTitle;
    private RecyclerView rv_wishlist;
    private String userId;
    private Button btnStartShop;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private WishlistAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(string.wishlist_title);
        rv_wishlist = findViewById(R.id.rv_wishlist);
        btnStartShop = findViewById(R.id.btn_start_shop);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        bottomNav();
        initFirestore();
        initRecyclerView();

    }

    private void initRecyclerView() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new WishlistAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    rv_wishlist.setVisibility(View.GONE);
                    setContentView(R.layout.empty_wishlist);
                    bottomNav();

                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rv_wishlist.setVisibility(View.VISIBLE);

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

        rv_wishlist.setLayoutManager(new LinearLayoutManager(WishlistActivity.this));
        rv_wishlist.setAdapter(mAdapter);
    }

    private void initFirestore() {

        mQuery = mFirestore.collection("Wishlist").whereEqualTo("userId", userId);
    }

    private void bottomNav() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set selected home
        bottomNavigationView.setSelectedItemId(R.id.wishlist);

        // item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case id.cart:
                        startActivity(new Intent(getApplicationContext(),CartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case id.wishlist:
                        return true;

                    case id.transaction:
                        startActivity(new Intent(getApplicationContext(),TransactionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            overridePendingTransition(0,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void onDeleteSelected(DocumentSnapshot productModel) {

        String deletedProduct = productModel.getId();
        mFirestore.collection("Wishlist").document(deletedProduct).delete();

        Toast.makeText(WishlistActivity.this, "Product with ID" + deletedProduct + "Deleted From Wishlist",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
