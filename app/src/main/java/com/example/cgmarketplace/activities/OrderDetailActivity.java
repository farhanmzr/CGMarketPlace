package com.example.cgmarketplace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cgmarketplace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;

public class OrderDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private FirebaseFirestore mFirestore;
    private DocumentReference mAddressRef, mUserRef;
    private FirebaseAuth mAuth;

    private Dialog alertDialog;
    private ImageView imgQuestion;
    private TextView tvQuestion, tv_title, tvSub_title;
    private Button btn_cancel, btn_confirm;

    private TextView tvTitle, tv_nama, tv_qty, tv_price, tv_width, tv_height, tv_dense, tv_material, tv_finishing, tv_full_name, tv_address, tv_city, tv_region, tv_zip_code, tv_country, tv_phone_number;
    private Button btn_dialog;
    private ImageView img_barang;
    private String userId;

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

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.order_detail);

        img_barang = findViewById(R.id.img_barang);
        tv_nama = findViewById(R.id.tv_nama);
        tv_qty = findViewById(R.id.tv_qty);
        tv_price = findViewById(R.id.tv_price);
        tv_width = findViewById(R.id.tv_width);
        tv_height = findViewById(R.id.tv_height);
        tv_dense = findViewById(R.id.tv_dense);
        tv_material = findViewById(R.id.tv_material);
        tv_finishing = findViewById(R.id.tv_finishing);
        tv_full_name = findViewById(R.id.tv_full_name);
        tv_address = findViewById(R.id.tv_address);
        tv_region = findViewById(R.id.tv_region);
        tv_zip_code = findViewById(R.id.tv_zip_code);
        tv_country = findViewById(R.id.tv_country);
        tv_phone_number = findViewById(R.id.tv_phone_number);

        initViews();
//        initData();


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


//    kok ra gelem ngundang data ya wkwk
//    private void initData() {
//
//        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "Document exists!");
//                        tv_full_name.setText(document.getString("fullName"));
//                        tv_phone_number.setText(document.getString("userTelephone"));
//
//                        mAddressRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    DocumentSnapshot document = task.getResult();
//                                    if (document.exists()) {
//                                        Log.d(TAG, "Document exists!");
//                                        tv_address.setText(document.getString("address"));
//                                        tv_city.setText(document.getString("city"));
//                                        tv_region.setText(document.getString("region"));
//                                        tv_zip_code.setText(document.getString("zipcode"));
//                                        tv_country.setText(document.getString("country"));
//
//                                    } else {
//                                        Log.d(TAG, "Document does not exist!");
//
//                                    }
//                                } else {
//                                    Log.d(TAG, "Failed with: ", task.getException());
//                                }
//                            }
//                        });
//
//                    } else {
//                        Log.d(TAG, "Document does not exist!");
//
//                    }
//                } else {
//                    Log.d(TAG, "Failed with: ", task.getException());
//                }
//            }
//        });
//    }

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
}
