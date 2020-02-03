package com.example.cgmarketplace.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.cgmarketplace.R;


public class NotConfirmedFragment extends Fragment implements View.OnClickListener {

    private Button btnConfirmPayment;

    public NotConfirmedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_not_confirmed, container, false);

        Button btnConfirmPayment = (Button) v.findViewById(R.id.btnConfirmPayment);
        btnConfirmPayment.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

    }

}
