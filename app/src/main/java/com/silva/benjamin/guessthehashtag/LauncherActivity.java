package com.silva.benjamin.guessthehashtag;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.silva.benjamin.guessthehashtag.util.Helper;

public class LauncherActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // Hide spinner
                ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);
                spinner.setVisibility(View.GONE);

                if(Helper.hasToken(LauncherActivity.this)){
                    goToApp();
                }else {
                    goToAuth();
                }
            }
        }, SPLASH_TIME_OUT);


    }
    private void goToApp(){
        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void goToAuth(){
        Intent intent = new Intent(LauncherActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }


}
