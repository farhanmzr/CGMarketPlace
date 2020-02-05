package com.example.cgmarketplace.adapters;

import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartAdapter extends FirestoreAdapter<CartAdapter.ViewHolder> {

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

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot cartModel);

        void onDeleteSelected(DocumentSnapshot cartModel);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_cart;
        private TextView tvNama;
        private TextView tvPrice;
        private Button btn_minus, btn_plus;
        private TextView tv_jumlah_cart;
        private ImageView delItem;
        private int totalPerItem;
        private int qtyItem = 1;

        public ViewHolder(View itemView) {
            super(itemView);
            img_cart = itemView.findViewById(R.id.img_cart);
            tvNama = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            delItem = itemView.findViewById(R.id.del_item);
            tv_jumlah_cart = itemView.findViewById(R.id.tv_jumlah_cart);

            tv_jumlah_cart.setText(String.valueOf(qtyItem));
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            final CartModel cartModel = snapshot.toObject(CartModel.class);

            qtyItem = cartModel.getQty();
            totalPerItem = cartModel.getPrice();

            String priceFormat = NumberFormat.getCurrencyInstance(Locale.US).format(totalPerItem * qtyItem);

            // Load image
            Glide.with(img_cart.getContext())
                    .load(cartModel.getImage())
                    .into(img_cart);

            tvNama.setText(cartModel.getName());
            tvPrice.setText(priceFormat);
            tv_jumlah_cart.setText(String.valueOf(qtyItem));

            // Click listener
            btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qtyItem += 1;

                    Map<String, Object> addQty = new HashMap<>();
                    addQty.put("qty", qtyItem);

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(userId).collection("Cart").document(snapshot.getId());
                    doc.set(addQty, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.w("Add Qty", "Success");

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Add Qty", "Error writing document", e);
                                }
                            });
                    Log.w("Total qty", String.valueOf(qtyItem));
                    Log.w("id", snapshot.getId());
                    tv_jumlah_cart.setText(String.valueOf(qtyItem));

                }
            });

            btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qtyItem -= 1;

                    Map<String, Object> addQty = new HashMap<>();
                    addQty.put("qty", qtyItem);

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(userId).collection("Cart").document(snapshot.getId());
                    doc.set(addQty, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.w("Min Qty", "Success");

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Min Qty", "Error writing document", e);
                                }
                            });

                    tv_jumlah_cart.setText(String.valueOf(qtyItem));
                }
            });

            if (qtyItem == 1) {

                btn_minus.animate().alpha(0).setDuration(0).start();
                btn_minus.setEnabled(false);
            } else {
                btn_minus.animate().alpha(1).setDuration(100).start();
                btn_minus.setEnabled(true);

            }

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
