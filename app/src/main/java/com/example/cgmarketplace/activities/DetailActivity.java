package com.example.cgmarketplace.activities;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.example.cgmarketplace.adapters.ViewPagerAdapter;
import com.example.cgmarketplace.model.ProductModel;
import com.example.cgmarketplace.model.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class DetailActivity extends AppCompatActivity
        implements EventListener<DocumentSnapshot> {

    public static final String KEY_PRODUCT_ID = "key_product_id";
    private static final String TAG = "Detail Activity";

    private FirebaseFirestore mFirestore;
    private DocumentReference mProdukRef;
    private DocumentReference mWishlistRef;
    private FirebaseAuth mAuth;
    private ListenerRegistration mProductRegistration;
    private String userId;

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private String[] img;
    private ImageButton addWishlist;

    private TextView tv_nama, tv_price, tv_desc, tv_width, tv_height, tv_dense, tv_finishing, tv_material, tv_details_1, tv_details_2, tv_details_3, tv_details_4;
    private String image1, image2, image3, productId;
    private Button btnAddToCart;

    boolean isWishlist;

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
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        addWishlist = findViewById(R.id.add_wishlist);

        productId = getIntent().getExtras().getString(KEY_PRODUCT_ID);
        if (productId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_PRODUCT_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

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


    private void onProductLoaded(final ProductModel product) {
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

        wishListState();
        img=new String[]{image1, image2, image3};
        //view pager code
        viewPager=(ViewPager) findViewById(R.id.pager);
        adapter=new ViewPagerAdapter(DetailActivity.this, img);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = mFirestore.collection("Users").document(userId).collection("Cart").document(productId);
                Map<String, Object> userCart = new HashMap<>();
                userCart.put("name", product.getName());
                userCart.put("image", product.getImage1());
                userCart.put("price", product.getPrice());
                userCart.put("qty", 1);
                documentReference.set(userCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailActivity.this, "Successfully Add To Cart",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "Failed Add To Cart",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
    });

        addWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWishlist) {
                    mWishlistRef.delete();
                    Toast.makeText(DetailActivity.this, "Product with ID" + productId + "Deleted From Wishlist",
                            Toast.LENGTH_LONG).show();

                } else {
                    Map<String, Object> userWishlist = new HashMap<>();
                    userWishlist.put("name", product.getName());
                    userWishlist.put("image", product.getImage1());
                    userWishlist.put("price", product.getPrice());
                    mWishlistRef.set(userWishlist).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailActivity.this, "Successfully Add To Wishlist",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActivity.this, "Failed Add To Wishlist",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                isWishlist = !isWishlist;
                wishListState();
            }
        });

    }

    private void wishListState() {

        mWishlistRef = mFirestore.collection("Users").document(userId).collection("Wishlist").document(productId);
        mWishlistRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        addWishlist.setBackgroundResource(R.drawable.ic_love);
                        isWishlist = true;
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        addWishlist.setBackgroundResource(R.drawable.ic_default_love);
                        isWishlist = false;
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }
}
