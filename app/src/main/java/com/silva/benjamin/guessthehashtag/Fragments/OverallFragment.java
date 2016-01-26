package com.silva.benjamin.guessthehashtag.Fragments;

import com.silva.benjamin.guessthehashtag.R;

/**
 * Created by benjamin on 12/10/15.
 */
public class OverallFragment extends WeeklyFragment {

    public OverallFragment() {
        // Required empty public constructor
        setmTabName(R.string.overall);
        setmTabType("OVERALL");
        setOrderBy("score");
    }
}
