package com.silva.benjamin.guessthehashtag.Fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.silva.benjamin.guessthehashtag.R;
import com.silva.benjamin.guessthehashtag.ResultsActivity;
import com.silva.benjamin.guessthehashtag.models.User;
import com.silva.benjamin.guessthehashtag.util.AnalyticsApplication;
import com.silva.benjamin.guessthehashtag.util.Helper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by benjamin on 12/10/15.
 */
public class ProfileFragment extends PagerFragment {
    private TextView mFullName;
    private TextView mUsername;
    private TextView mScore;
    private TextView mMaxStreak;
    private TextView mRank;

    private ImageView mProfilePic;
    private RelativeLayout mProfileLoadingWrapper;
    private ScrollView mProfileContentWrapper;
    private TextView mWeekScore;
    private Tracker mTracker;
    private Firebase mRootRef;

    public ProfileFragment() {
        setmTabName(R.string.profile);
        setmTabType("PROFILE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mFullName = (TextView) rootView.findViewById(R.id.full_name);
        mUsername = (TextView) rootView.findViewById(R.id.username);
        mScore = (TextView) rootView.findViewById(R.id.score);
        mWeekScore = (TextView) rootView.findViewById(R.id.week_score);
        mMaxStreak = (TextView) rootView.findViewById(R.id.max_streak);
        mRank = (TextView) rootView.findViewById(R.id.rank);
        mProfilePic = (ImageView) rootView.findViewById(R.id.profile_pic);
        mProfileLoadingWrapper = (RelativeLayout) rootView.findViewById(R.id.profile_loading_wrapper);
        mProfileContentWrapper = (ScrollView) rootView.findViewById(R.id.profile_content_wrapper);


        mFullName.setText(Helper.currentUser.getFull_name());
        mUsername.setText(Helper.currentUser.getUsername());
        mScore.setText("" + Helper.currentUser.getScore());
        mWeekScore.setText("" + Helper.currentUser.getWeek_score());
        mMaxStreak.setText("" + Helper.currentUser.getMax_streak());
        mRank.setText("--");
        Picasso.with(getActivity()).load(Helper.currentUser.getProfile_picture()).into(mProfilePic, new Callback() {
            @Override
            public void onSuccess() {
                mProfileLoadingWrapper.setVisibility(View.GONE);
                mProfileContentWrapper.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                ((ResultsActivity) getActivity()).showAToast(getString(R.string.oh_no_an_error_occurred), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
                mProfileLoadingWrapper.setVisibility(View.GONE);
            }
        });
        mRootRef = new Firebase("https://guessthehashtag.firebaseio.com/data");
        mRootRef.authWithCustomToken("91cvapZgSdVcjyvrepGKS2nSgDDFAiiDBLDl97Rx", new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

            }
        });
        Query queryRef = mRootRef.child("users").orderByChild("score");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long counter = snapshot.getChildrenCount();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User u = userSnapshot.getValue(User.class);
                    if (u.getUsername().equals(Helper.currentUser.getUsername())) {
                        mRank.setText("" + counter);
                        break;
                    }
                    counter--;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        mTracker.setScreenName("ProfileFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }
}
