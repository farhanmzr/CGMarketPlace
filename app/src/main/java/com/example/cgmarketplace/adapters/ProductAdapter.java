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
import com.example.cgmarketplace.model.ProductModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductAdapter extends FirestoreAdapter<ProductAdapter.ViewHolder> {

    private OnProductSelectedListener mListener;

    public ProductAdapter(Query query, OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_discover, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot productModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView discover_img;
        private TextView title;
        private TextView subtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            discover_img = itemView.findViewById(R.id.discover_img);
            title = itemView.findViewById(R.id.discover_title);
            subtitle = itemView.findViewById(R.id.discover_subtitle);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            ProductModel productModel = snapshot.toObject(ProductModel.class);

            String priceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(productModel.getPrice());

            // Load image
            Glide.with(discover_img.getContext())
                    .load(productModel.getImage1())
                    .into(discover_img);

            title.setText(productModel.getName());
            subtitle.setText(priceFormat);

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
