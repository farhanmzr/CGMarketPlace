package com.example.cgmarketplace.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cgmarketplace.fragment.ConfirmedFragment;
import com.example.cgmarketplace.fragment.DeliverFragment;
import com.example.cgmarketplace.fragment.NotConfirmedFragment;

public class PageAdapter  extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NotConfirmedFragment();
            case 1:
                return new ConfirmedFragment();
            case 2:
                return new DeliverFragment();
                default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
