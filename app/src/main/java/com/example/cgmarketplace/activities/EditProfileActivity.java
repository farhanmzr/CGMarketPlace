package com.example.cgmarketplace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private FirebaseFirestore mFirestore;
    private DocumentReference mUserRef;
    private FirebaseAuth mAuth;

//    private EditText tveditPhoneNumber, tveditUsername, tveditPassword, tveditAddress;
//    private TextView tvTitle, tveditEmail;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
//        tvTitle = findViewById(R.id.tvTitle);
//        tvTitle.setText(R.string.tv_edit_profile);
//        tveditPhoneNumber = findViewById(R.id.tv_phone_number);
//        tveditUsername = findViewById(R.id.tv_username);
//        tveditEmail = findViewById(R.id.tv_edit_email);
//        tveditPassword = findViewById(R.id.tv_password);
//        tveditAddress = findViewById(R.id.tv_address);

//        initData();
    }

//    private void initData() {
//        mUserRef = mFirestore.collection("Users").document(userId);
//        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "Document exists!");
//                        tveditUsername.setText(document.getString("userName"));
//                        tveditEmail.setText(document.getString("userEmail"));
//                        tveditAddress.setText(document.getString("userAddress"));
//                        tveditPassword.setText(document.getString("userPass"));
//                        tveditPhoneNumber.setText(document.getString("userTelephone"));
//
//                    } else {
//                        Log.d(TAG, "Document does not exist!");
//                    }
//                } else {
//                    Log.d(TAG, "Failed with: ", task.getException());
//                }
//            }
//        });
//    }
}
