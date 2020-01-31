package com.example.cgmarketplace.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.OrderDetailAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailAdapter.OnProductSelectedListener{

    private static final String TAG = "ShippingAddressActivity";
    private FirebaseFirestore mFirestore;
    private DocumentReference mAddressRef, mUserRef;
    private FirebaseAuth mAuth;
    private Query mQuery;

    private Dialog alertDialog;
    private ImageView imgQuestion;
    private TextView tvQuestion, tv_title, tvSub_title;
    private Button btn_cancel, btn_confirm;
    private RecyclerView rv_order_detail;
    private OrderDetailAdapter mAdapter;

    private TextView tvTitle, tv_full_name, tv_address, tv_city, tv_region, tv_zip_code, tv_country, tv_phone_number, tv_total_price;
    private Button btn_dialog;
    private String userId;
    private int totalPriceCart = 0;
    private Double qtyItem, priceItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mAddressRef = mFirestore.collection("Users").document(userId).collection("Address").document("shipAddress");
        mUserRef = mFirestore.collection("Users").document(userId);
        mQuery = mFirestore.collection("Users").document(userId).collection("Cart");

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.order_detail);

        tv_full_name = findViewById(R.id.tv_full_name);
        tv_address = findViewById(R.id.tv_address);
        tv_region = findViewById(R.id.tv_region);
        tv_zip_code = findViewById(R.id.tv_zip_code);
        tv_city = findViewById(R.id.tv_city);
        tv_country = findViewById(R.id.tv_country);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        rv_order_detail = findViewById(R.id.rv_order_detail);
        tv_total_price = findViewById(R.id.tv_total_price);

        initViews();
        initData();
        initRecyclerView();
        initTotalPrice();
    }

    private void initViews() {

        initCustomDialog();
        initViewComponents();
    }

    private void initCustomDialog() {
        alertDialog = new Dialog(OrderDetailActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_payment);

        imgQuestion = alertDialog.findViewById(R.id.imgQuestion);
        tvQuestion = alertDialog.findViewById(R.id.tvQuestion);
        tvTitle = alertDialog.findViewById(R.id.tvTitle);
        tvSub_title = alertDialog.findViewById(R.id.tvSub_title);
        btn_cancel = alertDialog.findViewById(R.id.btn_cancel);
        btn_confirm = alertDialog.findViewById(R.id.btn_confirm);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, OrderInvoiceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initViewComponents() {
        btn_dialog = findViewById(R.id.btn_dialog);
        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
    }

    private void initRecyclerView() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new OrderDetailAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    rv_order_detail.setVisibility(View.GONE);
                    setContentView(R.layout.empty_cart);

                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rv_order_detail.setVisibility(View.VISIBLE);
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

        rv_order_detail.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
        rv_order_detail.setAdapter(mAdapter);
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

    private void initData() {

        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        tv_full_name.setText(document.getString("fullName"));
                        tv_phone_number.setText(document.getString("userTelephone"));

                        mAddressRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "Document exists!");
                                        tv_address.setText(document.getString("address"));
                                        tv_city.setText(document.getString("city"));
                                        tv_region.setText(document.getString("region"));
                                        tv_zip_code.setText(document.getString("zipcode"));
                                        tv_country.setText(document.getString("country"));

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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(),ShippingAddressActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            overridePendingTransition(0,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductSelected(DocumentSnapshot cartModel) {

        // Go to the details page for the selected restaurant
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_PRODUCT_ID, cartModel.getId());
        intent.putExtra("no-Button", true);

        startActivity(intent);
    }

    private void initTotalPrice() {

        CollectionReference docPrice = mFirestore.collection("Users").document(userId).collection("Cart");
        docPrice.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<DocumentSnapshot> listPrice = task.getResult().getDocuments();
                    listPrice.size();
                    for (int i = 0; i < listPrice.size(); i++) {

                        qtyItem = listPrice.get(i).getDouble("qty");
                        priceItem = listPrice.get(i).getDouble("price");
                        totalPriceCart += qtyItem * priceItem;
                        Log.w("qty item", String.valueOf(qtyItem)); //debug qty
                        Log.w("price item", String.valueOf(priceItem)); //debug total
                        Log.w("total", String.valueOf(totalPriceCart)); //debug price total

                    }

                    String totalPriceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(totalPriceCart);
                    tv_total_price.setText(totalPriceFormat);
                    totalPriceCart = 0;
                }
            }
        });
    }
}
