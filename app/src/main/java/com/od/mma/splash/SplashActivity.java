package com.od.mma.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.od.mma.R;
import com.od.mma.loginregistration.LoginRegistrationActivity;

public class SplashActivity extends FragmentActivity {

    private static final int SPLASH_TIME_OUT = 1000;
    static SplashActivity splashActivity = null;
    Handler mHideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideNavigationBar();
        mHideHandler.postDelayed(mHideRunnable, SPLASH_TIME_OUT);
        splashActivity = this;
    }

    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            gotoLandingActivity();
        }
    };

    public void gotoLandingActivity() {
        Intent i = new Intent(SplashActivity.this, LoginRegistrationActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, 0);

        finish();
    }

    public void hideNavigationBar() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                /*| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY*/;

        if (currentApiVersion >= 17) {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
