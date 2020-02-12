package com.example.cgmarketplace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.ProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductSelectedListener{

    private static final String TAG = "MainActivity";
    private static final int LIMIT = 50;

    private SearchView search;

    BottomNavigationView bottomNavigationView;
    private ImageView img_profile, img_seats, img_bedroom, img_allcat, img_mirror, img_table, img_cabinet, imgEmptySearch;
    private TextView hello_user, titleCategory, textEmptySearch, textEmptySearch2;
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
        img_mirror = findViewById(R.id.category_mirror);
        img_table = findViewById(R.id.category_table);
        img_allcat = findViewById(R.id.all_category);
        titleCategory = findViewById(R.id.tvTitleCategory);
        search = findViewById(R.id.searchView);


        discover_recyclerview = findViewById(R.id.discover_recyclerview);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mQuery = mFirestore.collection("Produk");

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        categorySeats();
        categoryBedroom();
        categoryTable();
        categoryMirror();
        allCategory();
        updateUI(currentUser);


        bottomNav();

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(MainActivity.this, ProfileActivity.class);
                gotoprofile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(gotoprofile);
                finish();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.w("Query", query);
                Intent catalog = new Intent(MainActivity.this, CatalogActivity.class);
                catalog.putExtra(CatalogActivity.KEY_PRODUCT_CATEGORY, "Search Result");
                catalog.putExtra(CatalogActivity.KEY_SEARCH_QUERY, query);
                startActivity(catalog);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    private void categoryBedroom() {

        img_bedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent catalog = new Intent(MainActivity.this, CatalogActivity.class);
                catalog.putExtra(CatalogActivity.KEY_PRODUCT_CATEGORY, "Bedroom");
                startActivity(catalog);

            }
        });
    }

    private void categorySeats() {

        img_seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent catalog = new Intent(MainActivity.this, CatalogActivity.class);
                catalog.putExtra(CatalogActivity.KEY_PRODUCT_CATEGORY, "Seats");
                startActivity(catalog);

            }
        });
    }

    private void categoryTable() {

        img_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent catalog = new Intent(MainActivity.this, CatalogActivity.class);
                catalog.putExtra(CatalogActivity.KEY_PRODUCT_CATEGORY, "Table");
                startActivity(catalog);

            }
        });
    }

    private void allCategory() {

        img_allcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent catalog = new Intent(MainActivity.this, CatalogActivity.class);
                catalog.putExtra(CatalogActivity.KEY_PRODUCT_CATEGORY, "All");
                startActivity(catalog);

            }
        });
    }

    private void categoryMirror() {

        img_mirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent catalog = new Intent(MainActivity.this, CatalogActivity.class);
                catalog.putExtra(CatalogActivity.KEY_PRODUCT_CATEGORY, "Mirror");
                startActivity(catalog);

            }
        });
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

            Glide.with(img_profile.getContext())
                    .load(user.getPhotoUrl())
                    .into(img_profile);
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
                        startActivity(new Intent(getApplicationContext(),CartActivity.class).setFlags(FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.wishlist:
                        startActivity(new Intent(getApplicationContext(),WishlistActivity.class).setFlags(FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.transaction:
                        startActivity(new Intent(getApplicationContext(),TransactionActivity.class).setFlags(FLAG_ACTIVITY_CLEAR_TOP));
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
