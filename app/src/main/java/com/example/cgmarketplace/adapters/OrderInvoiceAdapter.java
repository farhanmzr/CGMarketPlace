package com.example.cgmarketplace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.example.cgmarketplace.model.CartModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.util.Locale;

public class OrderInvoiceAdapter extends FirestoreAdapter<OrderInvoiceAdapter.ViewHolder> {

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot cartModel);

    }

    private OrderInvoiceAdapter.OnProductSelectedListener mListener;

    public OrderInvoiceAdapter(Query query, OrderInvoiceAdapter.OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public OrderInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new OrderInvoiceAdapter.ViewHolder(inflater.inflate(R.layout.item_order_invoice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nama, tv_qty, tv_price, tv_total_price;
        int totalPrice = 0;
        private int totalPerItem;
        private int qtyItem;



        public ViewHolder(View itemView) {
            super(itemView);
            tv_nama = itemView.findViewById(R.id.tvNama);
            tv_price = itemView.findViewById(R.id.tvUnit_price);
            tv_qty = itemView.findViewById(R.id.tvQty);
            tv_total_price = itemView.findViewById(R.id.tvPrice);

        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            CartModel cartModel = snapshot.toObject(CartModel.class);

            qtyItem = cartModel.getQty();
            totalPerItem = cartModel.getPrice();
            String totalPriceUnit = NumberFormat.getCurrencyInstance(Locale.US).format(totalPerItem * qtyItem);

            String priceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(cartModel.getPrice());
            totalPrice +=cartModel.getPrice();

            tv_nama.setText(cartModel.getName());
            tv_qty.setText(String.valueOf(cartModel.getQty()));
            tv_price.setText(priceFormat);
            tv_total_price.setText(totalPriceUnit);

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

