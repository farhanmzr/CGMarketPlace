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
import com.example.cgmarketplace.model.CartModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.util.Locale;

public class CartAdapter extends FirestoreAdapter<CartAdapter.ViewHolder> {

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot cartModel);
        void onDeleteSelected(DocumentSnapshot cartModel);

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_cart;
        private TextView tvNama;
        private TextView tvPrice;
        private Button btn_minus, btn_plus;
        private TextView tv_jumlah_cart;
        private ImageView delItem;
        private Integer valueJumlahCart = 1;


        public ViewHolder(View itemView) {
            super(itemView);
            img_cart = itemView.findViewById(R.id.img_cart);
            tvNama = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            delItem = itemView.findViewById(R.id.del_item);
            tv_jumlah_cart = itemView.findViewById(R.id.tv_jumlah_cart);

            tv_jumlah_cart.setText(valueJumlahCart.toString());


        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            CartModel cartModel = snapshot.toObject(CartModel.class);

            String priceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(cartModel.getPrice());

            // Load image
            Glide.with(img_cart.getContext())
                    .load(cartModel.getImage())
                    .into(img_cart);

            tvNama.setText(cartModel.getName());
            tvPrice.setText(priceFormat);

            //
            btn_minus.animate().alpha(0).setDuration(0).start();
            btn_minus.setEnabled(false);

            // Click listener
            btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    valueJumlahCart+=1;
                    tv_jumlah_cart.setText(valueJumlahCart.toString());
                    if (valueJumlahCart > 1) {
                        btn_minus.animate().alpha(1).setDuration(100).start();
                        btn_minus.setEnabled(true);
                    }

                }
            });

            btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    valueJumlahCart-=1;
                    tv_jumlah_cart.setText(valueJumlahCart.toString());
                    if (valueJumlahCart < 2) {
                        btn_minus.animate().alpha(0).setDuration(100).start();
                        btn_minus.setEnabled(false);
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onProductSelected(snapshot);
                    }
                }
            });

            delItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeleteSelected(snapshot);
                    }
                }
            });
        }

    }
}
