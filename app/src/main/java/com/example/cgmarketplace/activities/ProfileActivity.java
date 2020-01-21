package com.example.cgmarketplace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.example.cgmarketplace.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private FirebaseFirestore mFirestore;
    private DocumentReference mUserRef;
    private FirebaseAuth mAuth;

    private TextView tvTitle, tvPhoneNumber, tvUsername, tvEmail, tvPassword, tvAddress;
    private ImageView img_profile;
    private Button btn_logout;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.profile_title);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvPassword = findViewById(R.id.tv_password);
        tvAddress = findViewById(R.id.tv_address);
        img_profile = findViewById(R.id.img_profile);
        btn_logout = findViewById(R.id.btn_logout);

        initData();

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(edit);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }


    private void initData() {
        mUserRef = mFirestore.collection("Users").document(userId);
        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        tvUsername.setText(document.getString("userName"));
                        tvEmail.setText(document.getString("userEmail"));
                        tvAddress.setText(document.getString("userAddress"));
                        tvPassword.setText(document.getString("userPass"));
                        tvPhoneNumber.setText(document.getString("userTelephone"));

                        Glide.with(img_profile.getContext())
                                .load(document.getString("userImg"))
                                .into(img_profile);

                    } else {
                        Log.d(TAG, "Document does not exist!");
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
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
}
