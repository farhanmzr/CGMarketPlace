package com.example.cgmarketplace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.cgmarketplace.R;
import com.example.cgmarketplace.activities.DetailActivity;

public class ViewPagerAdapter extends PagerAdapter {

    int position = 3;
    private String[] image = {"image1", "image2", "image3"};
    private LayoutInflater inflater;
    private Context context;

    public ViewPagerAdapter(DetailActivity detailActivity, String[] img) {
        this.context = detailActivity;
        this.image = img;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView img_pager;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemview = inflater.inflate(R.layout.item_pager, container, false);
        img_pager = itemview.findViewById(R.id.img_pager);

        Glide.with(context)
                .load(image[position])
                .into(img_pager);

        //add item_pager to ViewPager
        container.addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
