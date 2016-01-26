package com.silva.benjamin.guessthehashtag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.silva.benjamin.guessthehashtag.models.Media;
import com.silva.benjamin.guessthehashtag.models.SearchData;
import com.silva.benjamin.guessthehashtag.models.User;
import com.silva.benjamin.guessthehashtag.models.UserData;
import com.silva.benjamin.guessthehashtag.util.AnalyticsApplication;
import com.silva.benjamin.guessthehashtag.util.Helper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    ImageView mInstaImage;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    ScrollView mContentWrapper;
    RelativeLayout mLoaderWrapper;
    Toast mToast;
    TextView mScoreText;
    TextView mStreakText;
    Firebase mCurrentUserRef;
    Firebase mRootRef;
    Firebase mWeekResetRef;
    boolean doesntHaveStreakYet = true;
    ImageView mResultsButton;
    boolean isFirstLoad = true;
    private ValueEventListener mGetUserListener;
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(getApplicationContext());
        mRootRef = new Firebase("https://guessthehashtag.firebaseio.com/data");
        mRootRef.authWithCustomToken("91cvapZgSdVcjyvrepGKS2nSgDDFAiiDBLDl97Rx", new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

            }
        });
        mButton1 = (Button) findViewById(R.id.hashtag1);
        mButton2 = (Button) findViewById(R.id.hashtag2);
        mButton3 = (Button) findViewById(R.id.hashtag3);
        mButton4 = (Button) findViewById(R.id.hashtag4);
        //Call<SearchData> call = Helper.service().listImages("-33.4456328","-70.6157308", Helper.getToken(this));
        mInstaImage = (ImageView) findViewById(R.id.insta_image);
        mContentWrapper = (ScrollView) findViewById(R.id.content_wrapper);
        mLoaderWrapper = (RelativeLayout) findViewById(R.id.loading_wrapper);
        mScoreText = (TextView) findViewById(R.id.score_text);
        mStreakText = (TextView) findViewById(R.id.streak_text);
        mScoreText.setText("--");
        mStreakText.setText("" + Helper.mCurrentStreak);
        mResultsButton = (ImageView) findViewById(R.id.results_img);
        mResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToResults();
            }
        });
        getUser();
        checkThings();


    }

    private void goToResults() {
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        startActivity(intent);
        finish();
    }

    public void play() {
        final String hashtag = Helper.getRandomHashTag();
        Log.d("HASHTAG", hashtag);
        Call<SearchData> call = Helper.service().listImagesUsingTag(hashtag, "20", Helper.getToken(this));
        call.enqueue(new Callback<SearchData>() {

            @Override
            public void onResponse(Response<SearchData> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    // images available
                    SearchData result = response.body();
                    if (result.getData().length > 0) {
                        ArrayList<Media> images = result.filterMedia("image");
                        Random rnd = new Random();
                        final Media selected = images.get(rnd.nextInt(images.size()));
                        Picasso.with(MainActivity.this).load(selected.getImages().getStandard_resolution().getUrl()).into(mInstaImage,
                                new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        ArrayList<String> tags = new ArrayList<String>(Arrays.asList(selected.getTags()));
                                        ArrayList<String> hashPool = Helper.getRandomHashtagOptions(hashtag, tags);
                                        final ArrayList<Button> buttons = new ArrayList<Button>();
                                        buttons.add(mButton1);
                                        buttons.add(mButton2);
                                        buttons.add(mButton3);
                                        buttons.add(mButton4);
                                        for (int i = 0; i < buttons.size(); i++) {
                                            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (((Button) v).getText().equals("#" + hashtag)) {
                                                        ((Button) v).setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.green_correct));
                                                        Helper.currentUser.setScore(Helper.currentUser.getScore() + 1 + Helper.mCurrentStreak);
                                                        Helper.currentUser.setWeek_score(Helper.currentUser.getWeek_score() + 1 + Helper.mCurrentStreak);
                                                        Helper.mCurrentStreak++;
                                                        if (Helper.mCurrentStreak == Helper.currentUser.getMax_streak() + 1) {
                                                            if (!doesntHaveStreakYet)
                                                                showAToast("New Streak record: " + Helper.mCurrentStreak + " in a row !", Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 100);
                                                        }
                                                    } else {
                                                        ((Button) v).setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red_incorrect));
                                                        for (Button b : buttons) {
                                                            if (b.getText().equals("#" + hashtag))
                                                                b.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green_correct));
                                                        }
                                                        if (Helper.mCurrentStreak > 0 && doesntHaveStreakYet)
                                                            doesntHaveStreakYet = false; //if fails but had a streak for first time
                                                        if (Helper.mCurrentStreak > Helper.currentUser.getMax_streak())
                                                            Helper.currentUser.setMax_streak(Helper.mCurrentStreak);
                                                        Helper.mCurrentStreak = 0;
                                                        if (Helper.currentUser.getScore() - 1 < 0)
                                                            Helper.currentUser.setScore(0);
                                                        else
                                                            Helper.currentUser.setScore(Helper.currentUser.getScore() - 1);
                                                        if (Helper.currentUser.getWeek_score() - 1 < 0)
                                                            Helper.currentUser.setWeek_score(0);
                                                        else
                                                            Helper.currentUser.setWeek_score(Helper.currentUser.getWeek_score() - 1);
                                                    }
                                                    mScoreText.setText("" + Helper.currentUser.getScore());
                                                    Map<String, Object> updateUser = new HashMap<>();
                                                    updateUser.put("score", Helper.currentUser.getScore());
                                                    updateUser.put("max_streak", Helper.currentUser.getMax_streak());
                                                    updateUser.put("week_score", Helper.currentUser.getWeek_score());

                                                    mCurrentUserRef.updateChildren(updateUser);

                                                    mStreakText.setText("" + Helper.mCurrentStreak);
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            for (Button b : buttons) {
                                                                b.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.primary_text_light));
                                                                b.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.yellow_btn));
                                                                b.setOnClickListener(null);
                                                            }
                                                            mLoaderWrapper.setVisibility(View.VISIBLE);
                                                            mContentWrapper.setVisibility(View.GONE);
                                                            play();
                                                        }
                                                    }, 800);

                                                }
                                            });
                                            buttons.get(i).setText("#" + hashPool.get(i));
                                        }
                                        mLoaderWrapper.setVisibility(View.GONE);
                                        mContentWrapper.setVisibility(View.VISIBLE);
                                        mResultsButton.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError() {
                                        showAToast("Oh no! an error occurred, retrying", Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
                                        play();
                                        return;
                                    }
                                });
                    }

                } else {
                    // error response, no access to resource?
                    showAToast("Oh no! an error occurred, retrying", Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
                    play();
                    return;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
                showAToast("Oh no! an error occurred, retrying", Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
                play();
                return;
            }
        });
    }

    public void showAToast(String message, int gravity, int xoffset, int yoffset) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        int x_offset = Math.round(xoffset * getResources().getDisplayMetrics().density);
        int y_offset = Math.round(yoffset * getResources().getDisplayMetrics().density);
        mToast.setGravity(gravity, x_offset, y_offset);
        mToast.show();
    }

    private void displayAlert(String title, String message, Runnable positiveFunction, Runnable negativeFunction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message)
                .setTitle(title);
        if (positiveFunction != null) {
            final Runnable positive = positiveFunction;
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    positive.run();
                }
            });
        }
        if (negativeFunction != null) {
            final Runnable negative = negativeFunction;
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    negative.run();
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        displayAlert(getString(R.string.close_alert_title), "You are about to quit the game and loose your current streak, are you sure you want to quit?", new Runnable() {
            @Override
            public void run() {
                MainActivity.super.onBackPressed();
            }
        }, new Runnable() {
            @Override
            public void run() {
                //void run, nothing in here on purpose.
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo i = cm.getActiveNetworkInfo();
        if ((i == null) || (!i.isConnected())) {
            showAToast("Error: No internet connection", Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
            return false;
        }
        return true;
    }

    public void getUser() {
        if (isOnline()) {
            if (!Helper.hasUsername(MainActivity.this)) {
                Call<UserData> call = Helper.service().getCurrentUser(Helper.getToken(this));
                call.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Response<UserData> response, Retrofit retrofit) {
                        UserData current_user_data = response.body();
                        final User current_user = current_user_data.getData();
                        Helper.saveCurrentUserName(MainActivity.this, current_user.getUsername());
                        Log.d("RETROFIT", "Called instagram to get username");
                        getFirebaseUser(current_user);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showAToast("Oh no! an error occurred, retrying", Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, -20);
                        getUser();
                    }
                });
            } else {
                User aux = new User();
                String username = Helper.getCurrentUserName(MainActivity.this);
                aux.setUsername(username);
                Log.d("APP", "Username " + username + " was already saved in SharedPreferences");
                getFirebaseUser(aux);
            }


        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getUser();
                }
            }, 3000);
        }
    }

    private void getFirebaseUser(final User current_user) {
        mCurrentUserRef = mRootRef.child("users").child(current_user.getUsername());
        mGetUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("FIREBASE", "Called firebase to get user " + current_user.getUsername());

                if (snapshot.exists()) {
                    Log.d("FIREBASE", "User " + current_user.getUsername() + " was registered before");
                    Helper.currentUser = snapshot.getValue(User.class); // addValueEventListenerwill reset the week score in case its monday
                    if (Helper.currentUser.getMax_streak() > 0)
                        doesntHaveStreakYet = false;
                } else {
                    Log.d("FIREBASE", "User " + current_user.getUsername() + " didn't exist");
                    Helper.currentUser = current_user;
                    Helper.currentUser.setScore(0);
                    Helper.currentUser.setMax_streak(0);
                    Helper.currentUser.setWeek_score(0);
                    mCurrentUserRef.setValue(Helper.currentUser);

                }
                mScoreText.setText("" + Helper.currentUser.getScore());
                if (isFirstLoad) play();
                isFirstLoad = false;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("FIREBASE", "The read failed: " + firebaseError.getMessage());
            }
        };
        mCurrentUserRef.addValueEventListener(mGetUserListener);
    }

    private void checkThings() {
        Calendar cal = Calendar.getInstance();
        boolean monday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
        mWeekResetRef = mRootRef.child("settings").child("week_reset");
        if (monday) {
            mWeekResetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean is_reseted = snapshot.getValue(boolean.class);
                    if (!is_reseted) {
                        updateAllWeekScores();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else {
            mWeekResetRef.setValue(false);
        }
    }

    private void updateAllWeekScores() {
        mRootRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> week_scores = new HashMap<String, Object>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    week_scores.put(userSnapshot.child("username").getValue() + "/week_score", 0);
                }
                mRootRef.child("users").updateChildren(week_scores);
                mWeekResetRef.setValue(true);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        mCurrentUserRef.removeEventListener(mGetUserListener);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }
}
