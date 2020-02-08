package com.example.cgmarketplace.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ConfirmPaymentActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmedPaymentActivity";
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private DocumentReference mUserRef, mPaymentRef;
    private FirebaseAuth mAuth;
    private Query mQuery;
    FirebaseStorage storage;
    StorageReference storageReference;

    private Dialog alertDialog;
    private TextView tvTitle, tvSub_title;
    private Button btn_cancel, btn_confirm;
    private ImageView imgUpload, changeImg_upload;
    private String userId, orderId;

    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

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
        mPaymentRef = mFirestore.collection("Users").document(userId).collection("Payment").document(orderId);
        mQuery = mFirestore.collection("Users").document(userId).collection("Orders").document(orderId).collection("paymentOrder");


        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.confirm_payment);

        btnUpload = findViewById(R.id.btnUpload);

        initViews();

    }

    private void initViews() {
        initCustomDialog();
        initViewComponents();
    }

    private void initCustomDialog() {
        alertDialog = new Dialog(ConfirmPaymentActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_confirm_payment);

        imgUpload = alertDialog.findViewById(R.id.imgUpload);
        tvTitle = alertDialog.findViewById(R.id.tvTitle);
        changeImg_upload = alertDialog.findViewById(R.id.changeImg_upload);
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
                Intent gotosuccess = new Intent(ConfirmPaymentActivity.this,SuccessUploadPaymentActivity.class);
                alertDialog.dismiss();
                startActivity(gotosuccess);
                finish();
            }
        });

        Glide.with(imgUpload.getContext())
                .load(user.getPhotoUrl())
                .into(imgUpload);

        changeImg_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
                UploadImg();
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
                            = new ProgressDialog(ConfirmPaymentActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    changeImg_upload.setVisibility(View.INVISIBLE);

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
                                                UploadTask.TaskSnapshot taskSnapshot)
                                        {
                                            // Image uploaded successfully
                                            // Dismiss dialog
                                            progressDialog.dismiss();

                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();


                                                    user.updateProfile(profileUpdate)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                class TAG {
                                                                }

                                                                @SuppressLint("LongLogTag")
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Log.d(TAG, "Success Upload Payment");
                                                                }
                                                            });
                                                }
                                            });
                                        }
                                    })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {

                                    // Error, Image not uploaded
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(ConfirmPaymentActivity.this,
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
                                                UploadTask.TaskSnapshot taskSnapshot)
                                        {
                                            double progress
                                                    = (100.0
                                                    * taskSnapshot.getBytesTransferred()
                                                    / taskSnapshot.getTotalByteCount());
                                            progressDialog.setMessage(
                                                    "Uploaded "
                                                            + (int)progress + "%");
                                        }
                                    });
                }
            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

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
                imgUpload.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void initViewComponents() {
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(),TransactionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            overridePendingTransition(0,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
