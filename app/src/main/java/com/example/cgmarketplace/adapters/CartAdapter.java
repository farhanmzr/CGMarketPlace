package com.example.cgmarketplace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.example.cgmarketplace.model.ProductModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.util.Locale;

public class CartAdapter extends FirestoreAdapter<CartAdapter.ViewHolder> {

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot productModel);

    }

    private OnProductSelectedListener mListener;

    public CartAdapter(Query query, CartAdapter.OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CartAdapter.ViewHolder(inflater.inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_cart;
        TextView tvNama;
        TextView tvPrice;
        Button btn_minus, btn_plus;
        TextView tv_jumlah_cart, tvTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            img_cart = itemView.findViewById(R.id.img_cart);
            tvNama = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            tv_jumlah_cart = itemView.findViewById(R.id.tv_jumlah_cart);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final CartAdapter.OnProductSelectedListener listener) {

            ProductModel productModel = snapshot.toObject(ProductModel.class);

            int valuejumlahcart = 1;

            String priceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(productModel.getPrice());

            // Load image
            Glide.with(img_cart.getContext())
                    .load(productModel.getImage1())
                    .into(img_cart);

            tvNama.setText(productModel.getName());
            tvPrice.setText(priceFormat);
            tv_jumlah_cart.setText(String.valueOf(valuejumlahcart));
            // secara default, we hide btn_minus
            btn_minus.animate().alpha(0).setDuration(300).start();
            btn_minus.setEnabled(false);

            // Click listener
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