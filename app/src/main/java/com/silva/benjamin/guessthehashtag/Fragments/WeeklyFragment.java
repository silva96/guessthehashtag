package com.silva.benjamin.guessthehashtag.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.silva.benjamin.guessthehashtag.R;
import com.silva.benjamin.guessthehashtag.models.User;
import com.silva.benjamin.guessthehashtag.util.AnalyticsApplication;
import com.silva.benjamin.guessthehashtag.util.DividerItemDecoration;
import com.silva.benjamin.guessthehashtag.util.UserListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by benjamin on 12/10/15.
 */
public class WeeklyFragment extends PagerFragment {

    private Firebase mRootRef;
    private ArrayList<User> mUsersDataset;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private UserListAdapter mAdapter;
    private RelativeLayout mLoadingWrapper;
    private String orderBy;
    private Tracker mTracker;

    public WeeklyFragment() {
        setmTabName("WEEKLY");
        setOrderBy("week_score");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_weekly, container, false);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        Firebase.setAndroidContext(getActivity());
        mRootRef =  new Firebase("https://guessthehashtag.firebaseio.com/data");
        mRootRef.authWithCustomToken("91cvapZgSdVcjyvrepGKS2nSgDDFAiiDBLDl97Rx", new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

            }
        });
        mUsersDataset  = new ArrayList<>();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.week_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        mLoadingWrapper = (RelativeLayout) rootView.findViewById(R.id.week_loading_wrapper);
        renderData();
        return rootView;
    }

    private void renderData(){
        Query queryRef = mRootRef.child("users").orderByChild(getOrderBy()).limitToLast(100);//we need to iterate them backwards
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    mUsersDataset.add(userSnapshot.getValue(User.class));
                }
                Collections.reverse(mUsersDataset);
                mAdapter = new UserListAdapter(mUsersDataset, getActivity(), getmTabName());
                mRecyclerView.setAdapter(mAdapter);
                mLoadingWrapper.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        mTracker.setScreenName(getmTabName()+"Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }
}
