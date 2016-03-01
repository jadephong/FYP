package com.example.fyp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    public void onCreate(Bundle icicle) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);


        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionManager sessionManager = new SessionManager();
                String username = sessionManager.getUserName(SplashScreen.this).toString();
                Intent mainIntent;
                if (username.length() == 0) {
                    mainIntent = new Intent(SplashScreen.this, Login.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                } else {
                    mainIntent = new Intent(SplashScreen.this, MainActivity2.class);
                    mainIntent.putExtra("username", username);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}