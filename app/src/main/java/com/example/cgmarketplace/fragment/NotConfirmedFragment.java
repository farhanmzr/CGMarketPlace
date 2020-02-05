package com.example.cgmarketplace.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.activities.ConfirmPaymentActivity;


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
        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity();
            }

            private void moveActivity() {
                Intent i = new Intent(getActivity(), ConfirmPaymentActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        return v;
    }



    @Override
    public void onClick(View v) {

    }

}
