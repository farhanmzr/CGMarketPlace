package com.example.cgmarketplace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class WishlistAdapter extends FirestoreAdapter<WishlistAdapter.ViewHolder> {

    private WishlistAdapter.OnProductSelectedListener mListener;

    public WishlistAdapter(Query query, WishlistAdapter.OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new WishlistAdapter.ViewHolder(inflater.inflate(R.layout.item_wishlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot productModel);

        void onDeleteSelected(DocumentSnapshot productModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView wishlist_img;
        private TextView tvNama;
        private TextView tvPrice;
        private ImageButton btn_wishlist;


        public ViewHolder(View itemView) {
            super(itemView);
            wishlist_img = itemView.findViewById(R.id.wishlist_img);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btn_wishlist = itemView.findViewById(R.id.btn_wishlist);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final WishlistAdapter.OnProductSelectedListener listener) {

            ProductModel productModel = snapshot.toObject(ProductModel.class);

            String priceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(productModel.getPrice());

            // Load image
            Glide.with(wishlist_img.getContext())
                    .load(productModel.getImage1())
                    .into(wishlist_img);

            tvNama.setText(productModel.getName());
            tvPrice.setText(priceFormat);
            btn_wishlist.setBackgroundResource(R.drawable.ic_love);


            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onProductSelected(snapshot);
                    }
                }
            });

            btn_wishlist.setOnClickListener(new View.OnClickListener() {
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