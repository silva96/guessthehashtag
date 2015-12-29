package com.silva.benjamin.guessthehashtag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.silva.benjamin.guessthehashtag.util.AnalyticsApplication;
import com.silva.benjamin.guessthehashtag.util.Helper;

public class AuthActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mProgressBarAuth;
    private TextView mLoadingtextAuth;
    private boolean comesFromLogout=false;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_auth);
        if(Helper.hasToken(AuthActivity.this)){
            goToApp();
        }
        else{
            mProgressBarAuth = (ProgressBar) findViewById(R.id.progress_bar_auth);
            mLoadingtextAuth = (TextView) findViewById(R.id.loading_text_auth);
            mWebView = (WebView) findViewById(R.id.login_web_view);
            CookieManager cookieManager = CookieManager.getInstance();

            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
                cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                    @Override
                    public void onReceiveValue(Boolean value) {
                        //do nothing
                    }
                });
            } else{
                cookieManager.removeAllCookie();
            }


            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setSaveFormData(false);

            final Activity activity = this;
            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    activity.setProgress(progress * 1000);
                }
            });
            mWebView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:(function() { document.getElementsByTagName('header')[0].style.display = 'none';" +
                            "document.querySelectorAll('.form-actions a')[0].style.display = 'none';" +
                            "document.querySelectorAll('.dialog')[0].style.boxShadow = 'none';" +
                            "document.querySelectorAll('.dialog')[0].style.border = 'none';  })()");
                    mProgressBarAuth.setVisibility(View.GONE);
                    mLoadingtextAuth.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // Here put your code
                    Log.d("My Webview", url);
                    if (url.contains("instagram.com")) {
                        Log.d("My Webview", "contains instagram.com");
                        return false;//will load the url
                    } else {
                        Helper.saveToken(url.split("=")[1], AuthActivity.this);
                        goToApp();
                        return true;
                    }
                }
            });
            loadIg();
        }


    }

    private void loadIg() {
        if(isOnline()) {
            mWebView.loadUrl("https://instagram.com/oauth/authorize/?client_id=" + Helper.CLIENT_ID + "&redirect_uri=" + Helper.CALLBACK_URL + "&response_type=token");
        }
        else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadIg();
                }
            }, 3000);
        }
    }

    private void goToApp(){
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo i = cm.getActiveNetworkInfo();
        if ((i == null) || (!i.isConnected())) {
            Toast toast = Toast.makeText(getBaseContext(),
                    "Error: No connection to Internet", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        mTracker.setScreenName("AuthActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }
}
