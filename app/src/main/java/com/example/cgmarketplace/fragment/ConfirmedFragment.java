package com.example.cgmarketplace.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.activities.OrderInvoiceActivity;
import com.example.cgmarketplace.adapters.TransactionAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class ConfirmedFragment extends Fragment implements TransactionAdapter.OnProductSelectedListener {

    private static final String TAG = "Not Confirm Fragment";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private TransactionAdapter mAdapter;

    private RecyclerView rvConfirmed;
    private String userId;
    private TextView textEmptySearch, textEmptySearch2;
    private ImageView imgEmptySearch;

    public ConfirmedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mQuery = mFirestore.collection("Orders");
        mQuery = mQuery.whereEqualTo("userId", userId).whereEqualTo("status", "Confirmed");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirmed, container, false);
        rvConfirmed = v.findViewById(R.id.rvConfirmed);
        textEmptySearch = v.findViewById(R.id.tv_confirm_empty);
        textEmptySearch2 = v.findViewById(R.id.tv_confirm_empty2);
        imgEmptySearch = v.findViewById(R.id.img_confirm);
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
                    rvConfirmed.setVisibility(View.GONE);
                    textEmptySearch.setVisibility(View.VISIBLE);
                    textEmptySearch2.setVisibility(View.VISIBLE);
                    imgEmptySearch.setVisibility(View.VISIBLE);

                    Log.w(TAG, "ItemCount = 0");
                } else {
                    rvConfirmed.setVisibility(View.VISIBLE);
                    textEmptySearch.setVisibility(View.GONE);
                    textEmptySearch2.setVisibility(View.GONE);
                    imgEmptySearch.setVisibility(View.GONE);
                    Log.w(TAG, "Show Produk");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Log.w(TAG, "Error" + e);
            }
        };

        rvConfirmed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvConfirmed.setAdapter(mAdapter);
    }


    @Override
    public void onProductSelected(DocumentSnapshot orderModel) {

        Intent intent = new Intent(getActivity(), OrderInvoiceActivity.class);
        intent.putExtra(OrderInvoiceActivity.KEY_ORDER_ID, orderModel.getId());
        intent.putExtra("no-Button", true);

        startActivity(intent);
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