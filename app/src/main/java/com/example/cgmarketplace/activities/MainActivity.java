package com.example.cgmarketplace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.ProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int LIMIT = 50;

    BottomNavigationView bottomNavigationView;
    private ImageView img_profile, img_seats, img_bedroom, img_allcat;
    private TextView hello_user, titleCategory;
    private RecyclerView discover_recyclerview;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        img_profile = findViewById(R.id.img_profile);
        img_seats = findViewById(R.id.category_seats);
        img_bedroom = findViewById(R.id.category_bedroom);
        img_allcat = findViewById(R.id.all_category);
        titleCategory = findViewById(R.id.tvTitleCategory);


        discover_recyclerview = findViewById(R.id.discover_recyclerview);
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize Firestore and the main RecyclerView
        initFirestore();
        categorySeats();
        categoryBedroom();
        updateUI(currentUser);


        bottomNav();

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(gotoprofile);
            }
        });

    }

    private void categoryBedroom() {

        img_bedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleCategory.setText("Bedroom");

            }
        });
    }

    private void categorySeats() {

        img_seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// sek Error wqwq
//                titleCategory.setText("Seating");
//
//                mFirestore = FirebaseFirestore.getInstance();
//
//                CollectionReference product = mFirestore.collection("Produk");
//
//                mQuery = product.whereEqualTo("category", "seats");

            }
        });
    }

    private void initFirestore() {

        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("Produk");

//          Query buat kategori
//        CollectionReference product = mFirestore.collection("Produk");
//
//        mQuery = product.whereEqualTo("category", "seats");
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
                    discover_recyclerview.setVisibility(View.GONE);

                    Log.w(TAG, "ItemCount = 0");
                } else {
                    discover_recyclerview.setVisibility(View.VISIBLE);

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

        discover_recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        discover_recyclerview.setAdapter(mAdapter);
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

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            initRecyclerView();
            hello_user = findViewById(R.id.hello_user);
            hello_user.setText(
                    String.format("%s%s", "Hello, ", user.getDisplayName()));
        } else {
            Intent i = new Intent(MainActivity.this, LandingPage1Activity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    private void bottomNav() {

        // set selected home
        bottomNavigationView.setSelectedItemId(R.id.home);

        // item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        return true;

                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(),CartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.wishlist:
                        startActivity(new Intent(getApplicationContext(),WishlistActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.transaction:
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
    public void onProductSelected(DocumentSnapshot productModel) {
        // Go to the details page for the selected restaurant
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_PRODUCT_ID, productModel.getId());

        startActivity(intent);
    }
}
