package com.silva.benjamin.guessthehashtag.Fragments;

import android.support.v4.app.Fragment;


public class PagerFragment extends Fragment {
    private int mTabName;
    private String mTabType;

    public String getmTabType() {
        return mTabType;
    }

    public void setmTabType(String mTabType) {
        this.mTabType = mTabType;
    }

    public int getmTabName() {
        return mTabName;
    }

    public void setmTabName(int mTabName) {
        this.mTabName = mTabName;
    }
}
