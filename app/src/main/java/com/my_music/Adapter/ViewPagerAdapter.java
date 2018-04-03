package com.my_music.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by MXY on 2018/3/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private List<Fragment> fragmentList;
    private String[] titles;


    public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> list, String[] mTab) {
        super(fragmentManager);
        this.fragmentList = list;
        this.titles = mTab;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position < fragmentList.size()) {
            fragment = fragmentList.get(position);
        } else {
            fragment = fragmentList.get(0);
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
