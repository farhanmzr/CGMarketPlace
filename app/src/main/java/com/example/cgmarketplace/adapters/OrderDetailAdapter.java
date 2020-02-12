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

public class OrderDetailAdapter extends FirestoreAdapter<OrderDetailAdapter.ViewHolder> {

    private OnProductSelectedListener mListener;

    public OrderDetailAdapter(Query query, OrderDetailAdapter.OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new OrderDetailAdapter.ViewHolder(inflater.inflate(R.layout.item_order_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot cartModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        int totalPrice = 0;
        private ImageView img_barang;
        private TextView tv_nama, tv_qty, tv_price, tv_width, tv_height, tv_dense, tv_material, tv_finishing, tv_total_price;


        public ViewHolder(View itemView) {
            super(itemView);
            img_barang = itemView.findViewById(R.id.img_barang);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_width = itemView.findViewById(R.id.tv_width);
            tv_height = itemView.findViewById(R.id.tv_height);
            tv_dense = itemView.findViewById(R.id.tv_dense);
            tv_material = itemView.findViewById(R.id.tv_material);
            tv_finishing = itemView.findViewById(R.id.tv_finishing);

        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            CartModel cartModel = snapshot.toObject(CartModel.class);

            String priceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(cartModel.getPrice());
            totalPrice += cartModel.getPrice();


            // Load image
            Glide.with(img_barang.getContext())
                    .load(cartModel.getImage())
                    .into(img_barang);

            tv_nama.setText(cartModel.getName());
            tv_price.setText(priceFormat);
            tv_qty.setText(String.valueOf(cartModel.getQty()));
            tv_width.setText(cartModel.getWidth());
            tv_height.setText(cartModel.getHeight());
            tv_dense.setText(cartModel.getDense());
            tv_material.setText(cartModel.getMaterial());
            tv_finishing.setText(cartModel.getFinish());

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
