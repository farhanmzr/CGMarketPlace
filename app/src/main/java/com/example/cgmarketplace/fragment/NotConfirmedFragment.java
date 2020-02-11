package com.example.cgmarketplace.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimatedImageDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.activities.ConfirmPaymentActivity;
import com.example.cgmarketplace.activities.OrderInvoiceActivity;
import com.example.cgmarketplace.adapters.TransactionAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class NotConfirmedFragment extends Fragment implements TransactionAdapter.OnProductSelectedListener {

    private static final String TAG = "Not Confirm Fragment";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private TransactionAdapter mAdapter;

    private RecyclerView rvNot_confirmed;
    private String userId;

    public NotConfirmedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mQuery = mFirestore.collection("Orders");
        mQuery = mQuery.whereEqualTo("userId", userId).whereEqualTo("status", "Not Confirmed");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_not_confirmed, container, false);

        rvNot_confirmed = v.findViewById(R.id.rvNot_confirmed);
        initRv();

        return v;
    }

    private void initRv() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new TransactionAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    rvNot_confirmed.setVisibility(View.GONE);



                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rvNot_confirmed.setVisibility(View.VISIBLE);
                    Log.w(TAG, "Show Produk");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Log.w(TAG, "Error" + e);
            }
        };

        rvNot_confirmed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNot_confirmed.setAdapter(mAdapter);
    }


    @Override
    public void onProductSelected(DocumentSnapshot orderModel) {
        final DocumentReference orderIdRef = orderModel.getReference();
        orderIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Intent intent = new Intent(getActivity(), OrderInvoiceActivity.class);
                    intent.putExtra(OrderInvoiceActivity.KEY_ORDER_ID, document.getString("orderId"));
                    intent.putExtra("no-Button", true);

                    startActivity(intent);
                }
            }
        });
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
}
