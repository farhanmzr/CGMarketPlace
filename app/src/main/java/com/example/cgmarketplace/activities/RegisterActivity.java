package com.example.cgmarketplace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cgmarketplace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    Button btn_register;
    ImageView ic_back;
    EditText input_username_reg, input_email_reg, input_password_reg, input_confirm_password;
    TextView tv_login;
    String username, email, password, confirm_password, userId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        btn_register = findViewById(R.id.btn_register);
        input_username_reg = findViewById(R.id.input_username_reg);
        input_email_reg = findViewById(R.id.input_email_reg);
        input_password_reg = findViewById(R.id.input_password_reg);
        input_confirm_password = findViewById(R.id.input_confirm_password);
        tv_login = findViewById(R.id.tv_login);
        ic_back = findViewById(R.id.ic_back);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotologin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(gotologin);
                finish();
                overridePendingTransition(0,0);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerUser();
        toLogin();
    }

    private void toLogin() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
    }

    private void registerUser() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = input_username_reg.getText().toString();
                email = input_email_reg.getText().toString();
                password = input_password_reg.getText().toString();
                confirm_password = input_confirm_password.getText().toString();


                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Masukan Username !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Masukan Email !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Format Email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Masukan Password !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confirm_password)) {
                    Toast.makeText(getApplicationContext(), "Masukan Password Confirm !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirm_password)) {
                    Toast.makeText(getApplicationContext(), "Password harus sama !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Daftar sukses, masuk ke Main Activity
                                    userId = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = db.collection("Users").document(userId);
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("userName", username);
                                    userData.put("userEmail", email);
                                    userData.put("userAddress", null);
                                    userData.put("userTelephone", null);
                                    documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            sendVerificationEmail();
                                        }
                                    });
                                } else {
                                    // Jika daftar gagal, memberikan pesan
                                    Toast.makeText(RegisterActivity.this, "Proses Pendaftaran gagal : " +  task.getException(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).build();

        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User Profile Updated");
                    }
                });
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this).create();
                            dialog.setTitle("Registration Successful");
                            dialog.setMessage("Registration successful! Please verify your email to activate your account.");
                            dialog.setButton(Dialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            dialog.show();
                            // after email is sent just logout the user and finish this activity

                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            Toast.makeText(RegisterActivity.this, "Please Verify Your Email! : " +  task.getException(),
                                    Toast.LENGTH_LONG).show();
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }
}
