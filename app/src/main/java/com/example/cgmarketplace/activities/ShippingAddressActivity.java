package com.example.cgmarketplace.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cgmarketplace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShippingAddressActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private FirebaseFirestore mFirestore;
    private DocumentReference mAddressRef, mUserRef;
    private FirebaseAuth mAuth;

    private TextView tvTitle;
    private Button btn_next;
    private EditText etFull_name, etAddress, etCity, etRegion, etZip_Code, etCountry, etPhone_Number;
    Button btnNext, btnSaveAddress;

    private String userId, address, city, region, zipCode, phone, fullname, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mAddressRef = mFirestore.collection("Users").document(userId).collection("Address").document("shipAddress");
        mUserRef = mFirestore.collection("Users").document(userId);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.shipping_address);

        btnNext = findViewById(R.id.btn_next);
        btnSaveAddress = findViewById(R.id.btn_save);
        etFull_name = findViewById(R.id.etFull_name);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etRegion = findViewById(R.id.etRegion);
        etZip_Code = findViewById(R.id.etZip_Code);
        etCountry = findViewById(R.id.etCountry);
        etPhone_Number = findViewById(R.id.etPhone_Number);
        btn_next = findViewById(R.id.btn_next);

        initData();
        saveAddress();
    }

    private void saveAddress() {

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                address = etAddress.getText().toString();
                city = etCity.getText().toString();
                region = etRegion.getText().toString();
                zipCode = etZip_Code.getText().toString();
                country = etCountry.getText().toString();
                fullname = etFull_name.getText().toString();
                phone = etPhone_Number.getText().toString();

                if (!Objects.equals(address, "") && !Objects.equals(city, "") && !Objects.equals(region, "") && !Objects.equals(zipCode, "") && !Objects.equals(country, "") && !Objects.equals(fullname, "") && !Objects.equals(phone, "")) {

                    Map<String, Object> addShippingAddress = new HashMap<>();
                    addShippingAddress.put("address", address);
                    addShippingAddress.put("city", city);
                    addShippingAddress.put("region", region);
                    addShippingAddress.put("zipcode", zipCode);
                    addShippingAddress.put("country", country);

                    mAddressRef.set(addShippingAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.w(TAG, "Successfully");

                            Map<String, Object> addUser = new HashMap<>();
                            addUser.put("fullName", fullname);
                            addUser.put("userTelephone", phone);

                            mUserRef.set(addUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.w(TAG, "Successfully");
                                    Toast.makeText(ShippingAddressActivity.this, "Address Update",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    btnNext.setClickable(true);
                                    btnNext.setBackgroundColor(R.drawable.bg_btn_blue);
                                    btn_next.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ShippingAddressActivity.this, OrderDetailActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
                } else {


                    Toast.makeText(ShippingAddressActivity.this, "All Field must be fill",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void initData() {

        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        etFull_name.setText(document.getString("fullName"));
                        etPhone_Number.setText(document.getString("userTelephone"));

                        mAddressRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "Document exists!");
                                        etAddress.setText(document.getString("address"));
                                        etCity.setText(document.getString("city"));
                                        etRegion.setText(document.getString("region"));
                                        etZip_Code.setText(document.getString("zipcode"));
                                        etCountry.setText(document.getString("country"));

                                    } else {
                                        Log.d(TAG, "Document does not exist!");
                                        btnNext.setClickable(false);
                                        btnNext.setBackgroundColor(Color.parseColor("#808080"));
                                    }
                                } else {
                                    Log.d(TAG, "Failed with: ", task.getException());
                                }
                            }
                        });

                    } else {
                        Log.d(TAG, "Document does not exist!");
                        btnNext.setClickable(false);
                        btnNext.setBackgroundColor(Color.parseColor("#808080"));
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
            startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            overridePendingTransition(0,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
