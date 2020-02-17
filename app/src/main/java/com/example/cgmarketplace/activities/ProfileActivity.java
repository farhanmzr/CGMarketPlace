package com.example.cgmarketplace.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private DocumentReference mUserRef;
    private FirebaseAuth mAuth;
    private Uri filePath;
    private TextView tvTitle;
    private EditText etFullName, etUsername, etEmail, etPhone_Number, etPassword, etNewPass, etNewConfirmPass, etAddress, etCity, etRegion, etZip_Code, etCountry;
    private ImageView img_profile, change_img;
    private ImageButton ic_edit_profile, ic_edit_address, ic_edit_password;
    private Button btn_logout;
    private String userId, fullname, username, email, phone, address, city, region, zipCode, country, recentPass, newPass, confirmPass;
    private boolean editProfile, editAddress, editPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editProfile = false;
        editAddress = false;
        editPass = false;


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mUserRef = mFirestore.collection("Users").document(userId);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.profile_title);
        img_profile = findViewById(R.id.img_profile);
        change_img = findViewById(R.id.change_img_user);
        btn_logout = findViewById(R.id.btn_logout);
        ic_edit_profile = findViewById(R.id.ic_edit_profile);
        ic_edit_address = findViewById(R.id.ic_edit_addressProfile);
        ic_edit_password = findViewById(R.id.ic_edit_password);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etNewPass = findViewById(R.id.etNew_password);
        etNewConfirmPass = findViewById(R.id.etConfirm_password);
        etPhone_Number = findViewById(R.id.etPhone_number);
        etUsername = findViewById(R.id.etUsername);
        etFullName = findViewById(R.id.etFull_name);
        etAddress = findViewById(R.id.etAddressProfile);
        etCity = findViewById(R.id.etCityProfile);
        etRegion = findViewById(R.id.etRegionProfile);
        etZip_Code = findViewById(R.id.etZip_codeProfile);
        etCountry = findViewById(R.id.etCountryProfile);

        Glide.with(img_profile.getContext())
                .load(user.getPhotoUrl())
                .into(img_profile);

        change_img.setVisibility(View.INVISIBLE);

        initDataProfile();
        initDataAddress();
        initDataPass();
        editDataAddress();
        editDataProfile();
        editDataPass();

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

        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

    }

    private void initDataPass() {


        etPassword.setEnabled(editPass);
        etPassword.setFocusableInTouchMode(editPass);
        etPassword.setFocusable(editPass);

        etNewPass.setEnabled(editPass);
        etNewPass.setFocusableInTouchMode(editPass);

        etNewConfirmPass.setEnabled(editPass);
        etNewConfirmPass.setFocusableInTouchMode(editPass);
    }

    private void editDataPass() {

        ic_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPass) {
                    recentPass = etPassword.getText().toString();
                    newPass = etNewPass.getText().toString();
                    confirmPass = etNewConfirmPass.getText().toString();

                    if (newPass.equals(confirmPass)) {

                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), recentPass);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Password updated");
                                                        ic_edit_password.setBackgroundResource(R.drawable.ic_edit_profile);
                                                        editPass = !editPass;
                                                        initDataPass();

                                                    } else {
                                                        Log.d(TAG, "Error password not updated");
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.d(TAG, "Error auth failed");
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(ProfileActivity.this, "Password do not match",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {

                    ic_edit_password.setBackgroundResource(R.drawable.img_save_changes);
                    editPass = !editPass;
                    initDataPass();

                }
            }
        });
    }

    private void editDataProfile() {

        ic_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editProfile) {

                    fullname = etFullName.getText().toString();
                    username = etUsername.getText().toString();
                    phone = etPhone_Number.getText().toString();

                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                    user.updateProfile(profileUpdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User Profile Updated");
                                }
                            });

                    Map<String, Object> profile = new HashMap<>();
                    profile.put("fullName", fullname);
                    profile.put("userTelephone", phone);

                    mUserRef.set(profile, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.w(TAG, "Successfully");
                            UploadImg();

                            ic_edit_profile.setBackgroundResource(R.drawable.ic_edit_profile);
                            change_img.setVisibility(View.INVISIBLE);
                            editProfile = !editProfile;
                            initDataProfile();
                            Toast.makeText(ProfileActivity.this,
                                    "Profile Changed",
                                    Toast.LENGTH_SHORT)
                                    .show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
                } else {

                    ic_edit_profile.setBackgroundResource(R.drawable.img_save_changes);
                    change_img.setVisibility(View.VISIBLE);
                    editProfile = !editProfile;
                    initDataProfile();
                }

            }
        });
    }

    private void initDataProfile() {

        etUsername.setEnabled(editProfile);
        etUsername.setFocusableInTouchMode(editProfile);
        etUsername.setFocusable(editProfile);

        etFullName.setEnabled(editProfile);
        etFullName.setFocusableInTouchMode(editProfile);

        etEmail.setEnabled(false);
        etEmail.setFocusableInTouchMode(false);
        etEmail.setFocusable(false);

        etPhone_Number.setEnabled(editProfile);
        etPhone_Number.setFocusableInTouchMode(editProfile);

        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        etFullName.setText(document.getString("fullName"));
                        etUsername.setText(user.getDisplayName());
                        etEmail.setText(user.getEmail());
                        etPhone_Number.setText(document.getString("userTelephone"));

                    } else {
                        Log.d(TAG, "Document does not exist!");
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }

    private void initDataAddress() {

        etAddress.setEnabled(editAddress);
        etAddress.setFocusableInTouchMode(editAddress);
        etAddress.setFocusable(editAddress);

        etCity.setEnabled(editAddress);
        etCity.setFocusableInTouchMode(editAddress);

        etRegion.setEnabled(editAddress);
        etRegion.setFocusableInTouchMode(editAddress);

        etZip_Code.setEnabled(editAddress);
        etZip_Code.setFocusableInTouchMode(editAddress);

        etCountry.setEnabled(editAddress);
        etCountry.setFocusableInTouchMode(editAddress);

        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document.getString("address") != null && document.getString("city") != null && document.getString("region") != null && document.getString("zipcode") != null && document.getString("country") != null) {
                        Log.d(TAG, "Document exists!");
                        etAddress.setText(document.getString("address"));
                        etCity.setText(document.getString("city"));
                        etRegion.setText(document.getString("region"));
                        etZip_Code.setText(document.getString("zipcode"));
                        etCountry.setText(document.getString("country"));

                    } else {
                        Log.d(TAG, "Document does not exist!");
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }

    private void editDataAddress() {

        ic_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAddress) {

                    address = etAddress.getText().toString();
                    city = etCity.getText().toString();
                    region = etRegion.getText().toString();
                    zipCode = etZip_Code.getText().toString();
                    country = etCountry.getText().toString();

                    Map<String, Object> addShippingAddress = new HashMap<>();
                    addShippingAddress.put("address", address);
                    addShippingAddress.put("city", city);
                    addShippingAddress.put("region", region);
                    addShippingAddress.put("zipcode", zipCode);
                    addShippingAddress.put("country", country);

                    mUserRef.set(addShippingAddress, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.w(TAG, "Successfully");
                            Toast.makeText(ProfileActivity.this, "Address Update",
                                    Toast.LENGTH_SHORT)
                                    .show();

                            ic_edit_address.setBackgroundResource(R.drawable.ic_edit_profile);
                            editAddress = !editAddress;
                            initDataAddress();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
                } else {

                    ic_edit_address.setBackgroundResource(R.drawable.img_save_changes);
                    editAddress = !editAddress;
                    initDataAddress();
                }

            }
        });
    }

    private void selectImage() {

// Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                img_profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            overridePendingTransition(0, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void UploadImg() {

        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "imagesUser/"
                                    + userId + "." + GetFileExtension(filePath));

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).setPhotoUri(uri).build();

                                            user.updateProfile(profileUpdate)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.d(TAG, "User Profile Updated");
                                                        }
                                                    });
                                        }
                                    });
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(ProfileActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }
}
