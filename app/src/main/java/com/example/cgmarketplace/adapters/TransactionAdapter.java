package com.example.cgmarketplace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.model.OrderModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;

public class TransactionAdapter extends FirestoreAdapter<TransactionAdapter.ViewHolder> {

    private OnProductSelectedListener mListener;

    public TransactionAdapter(Query query, TransactionAdapter.OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TransactionAdapter.ViewHolder(inflater.inflate(R.layout.item_not_confirmed, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot cartModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvPrice;
        private TextView tvOrderId;
        private TextView tvDate;
        private TextView tvAddress;
        private TextView tvCity;
        private TextView tvCountry;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_order);
            tvPrice = itemView.findViewById(R.id.tvTotal_price);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvCountry = itemView.findViewById(R.id.tvCountry);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            final OrderModel orderModel = snapshot.toObject(OrderModel.class);

            tvName.setText(orderModel.getName());
            tvPrice.setText(orderModel.getTotalOrder());
            tvOrderId.setText(snapshot.getId());
            tvAddress.setText(orderModel.getAddress());
            tvCity.setText(orderModel.getCity());
            tvCountry.setText(orderModel.getCountry());

            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            String date = dateFormat.format(orderModel.getDate());

            tvDate.setText(date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onProductSelected(snapshot);
                    }
                }
            });

        }

    }
}