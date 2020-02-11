package com.example.cgmarketplace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cgmarketplace.R;
import com.example.cgmarketplace.model.SearchModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<SearchModel> productNameList = null;
    private ArrayList<SearchModel> arraylist;

    public SearchViewAdapter (Context context, List<SearchModel> productNameList) {

        mContext = context;
        this.productNameList = productNameList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchModel>();
        this.arraylist.addAll(productNameList);

    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return productNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return productNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_list_search, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.name.setText(productNameList.get(position).getProductName());
        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productNameList.clear();
        if (charText.length() == 0) {
            productNameList.addAll(arraylist);
        } else {
            for (SearchModel wp : arraylist) {
                if (wp.getProductName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productNameList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
