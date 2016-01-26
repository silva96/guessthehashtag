package com.silva.benjamin.guessthehashtag.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

import com.silva.benjamin.guessthehashtag.Fragments.PagerFragment;



public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private  List<Fragment> mFragmentList = new ArrayList<>();


    private Context mContext;
    public ViewPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }


    public void addFragment(Fragment f){
        mFragmentList.add(f);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int tab_name = ((PagerFragment)mFragmentList.get(position)).getmTabName();
        return mContext.getString(tab_name);
    }
}