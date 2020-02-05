package com.example.cgmarketplace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.OrderInvoiceAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;

public class OrderInvoiceActivity extends AppCompatActivity implements OrderInvoiceAdapter.OnProductSelectedListener {
    public static final String KEY_ORDER_ID = "key_order_id";

    private static final String TAG = "ShippingAddressActivity";
    private FirebaseFirestore mFirestore;
    private DocumentReference mOrderRef, mUserRef;
    private FirebaseAuth mAuth;
    private Query mQuery;

    private TextView tvTitle, tv_id_order, tv_date, tv_total_price, tv_full_name, tv_address, tv_city, tv_region, tv_zip_code, tv_country, tv_phone_number;

    private Button btn_confirm;
    private RecyclerView rv_order_finish;
    private OrderInvoiceAdapter mAdapter;
    private String userId, orderId;
    private int totalPriceCart = 0;
    private Double qtyItem, priceItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_invoice);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderId = getIntent().getExtras().getString(KEY_ORDER_ID);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mOrderRef = mFirestore.collection("Users").document(userId).collection("Orders").document(orderId);
        mUserRef = mFirestore.collection("Users").document(userId);
        mQuery = mFirestore.collection("Users").document(userId).collection("Orders").document(orderId).collection("purchasedProduct");

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.order_invoice);

        tv_id_order = findViewById(R.id.tv_id_order);
        tv_date = findViewById(R.id.tv_date);

        rv_order_finish = findViewById(R.id.rv_order_finish);
        tv_total_price = findViewById(R.id.tv_total_price);

        tv_full_name = findViewById(R.id.tv_full_name);
        tv_address = findViewById(R.id.tv_address);
        tv_city = findViewById(R.id.tv_city);
        tv_region = findViewById(R.id.tv_region);
        tv_zip_code = findViewById(R.id.tv_zip_code);
        tv_country = findViewById(R.id.tv_country);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        btn_confirm = findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderInvoiceActivity.this, TransactionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        initData();
        initRecyclerView();
    }

    private void initData() {

        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        tv_full_name.setText(document.getString("fullName"));
                        tv_phone_number.setText(document.getString("userTelephone"));

                        mOrderRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "Document exists!");
                                        tv_id_order.setText(orderId);

                                        DateFormat dateFormat = DateFormat.getDateTimeInstance();
                                        String date = dateFormat.format(document.getDate("date"));
                                        tv_date.setText(date);
                                        tv_address.setText(document.getString("address"));
                                        tv_city.setText(document.getString("city"));
                                        tv_region.setText(document.getString("region"));
                                        tv_zip_code.setText(document.getString("zipcode"));
                                        tv_country.setText(document.getString("country"));
                                        tv_total_price.setText(document.getString("totalOrder"));

                                    } else {
                                        Log.d(TAG, "Document does not exist!");

                                    }
                                } else {
                                    Log.d(TAG, "Failed with: ", task.getException());
                                }
                            }
                        });

                    } else {
                        Log.d(TAG, "Document does not exist!");

                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new OrderInvoiceAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    rv_order_finish.setVisibility(View.GONE);
                    setContentView(R.layout.empty_cart);

                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rv_order_finish.setVisibility(View.VISIBLE);
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

        rv_order_finish.setLayoutManager(new LinearLayoutManager(OrderInvoiceActivity.this));
        rv_order_finish.setAdapter(mAdapter);
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




    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(),OrderDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            overridePendingTransition(0,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductSelected(DocumentSnapshot cartModel) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_PRODUCT_ID, cartModel.getId());
        intent.putExtra("no-Button", true);

        startActivity(intent);
    }
}


