package com.example.cgmarketplace.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.ViewPagerAdapter;
import com.example.cgmarketplace.model.ProductModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.NumberFormat;
import java.util.Locale;


public class DetailActivity extends AppCompatActivity
        implements EventListener<DocumentSnapshot> {

    public static final String KEY_PRODUCT_ID = "key_product_id";
    private static final String TAG = "Detail Activity";

    private FirebaseFirestore mFirestore;
    private DocumentReference mProdukRef;
    private ListenerRegistration mProductRegistration;

    ViewPager viewPager;
    PagerAdapter adapter;
    String[] img;

    private TextView tv_nama, tv_price, tv_desc, tv_width, tv_height, tv_dense, tv_finishing, tv_material, tv_details_1, tv_details_2, tv_details_3, tv_details_4;
    private String image1, image2, image3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_nama = findViewById(R.id.tv_nama);
        tv_price = findViewById(R.id.tv_price);
        tv_desc = findViewById(R.id.tv_desc);
        tv_width = findViewById(R.id.tv_width);
        tv_height = findViewById(R.id.tv_height);
        tv_dense = findViewById(R.id.tv_dense);
        tv_finishing = findViewById(R.id.tv_finishing);
        tv_material = findViewById(R.id.tv_material);
        tv_details_1 = findViewById(R.id.tv_details_1);
        tv_details_2 = findViewById(R.id.tv_details_2);
        tv_details_3 = findViewById(R.id.tv_details_3);
        tv_details_4 = findViewById(R.id.tv_details_4);

        String productId = getIntent().getExtras().getString(KEY_PRODUCT_ID);
        if (productId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_PRODUCT_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the db
        mProdukRef = mFirestore.collection("Produk")
                .document(productId);
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

    @Override
    public void onStart() {
        super.onStart();

        mProductRegistration = mProdukRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mProductRegistration != null) {
            mProductRegistration.remove();
            mProductRegistration = null;
        }
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

        if (e != null) {
            Log.w(TAG, "product:onEvent", e);
            return;
        }

        onProductLoaded(snapshot.toObject(ProductModel.class));
    }


    private void onProductLoaded(ProductModel product) {
        String priceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(product.getPrice());

        tv_nama.setText(product.getName());
        tv_price.setText(priceFormat);
        tv_desc.setText(product.getDesc());
        tv_finishing.setText(product.getFinish());
        tv_material.setText(product.getMaterial());
        tv_width.setText(product.getWidth());
        tv_height.setText(product.getHeight());
        tv_dense.setText(product.getDense());
        tv_details_1.setText(product.getDetails1());
        tv_details_2.setText(product.getDetails2());
        tv_details_3.setText(product.getDetails3());
        tv_details_4.setText(product.getDetails4());
        image1 = product.getImage1();
        image2 = product.getImage2();
        image3 = product.getImage3();

        img=new String[]{image1, image2, image3};
        //view pager code
        viewPager=(ViewPager) findViewById(R.id.pager);
        adapter=new ViewPagerAdapter(DetailActivity.this, img);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

    }
}
