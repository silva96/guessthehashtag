package com.silva.benjamin.guessthehashtag.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silva.benjamin.guessthehashtag.R;

/**
 * Created by benjamin on 12/10/15.
 */
public class OverallFragment extends WeeklyFragment {

    public OverallFragment() {
        // Required empty public constructor
        setmTabName("OVERALL");
        setOrderBy("score");
    }
}
