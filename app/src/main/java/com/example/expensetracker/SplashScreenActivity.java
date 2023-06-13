package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView logoBudgetMate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_LightStatusBar);
        setContentView(R.layout.activity_splash_screen);
        // Change Notification Bar Color
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.nonPureWhite));

        logoBudgetMate = findViewById(R.id.logoBudgetMate);
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);
        logoBudgetMate.startAnimation(fadeIn);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    java.util.concurrent.TimeUnit.SECONDS.sleep(2);
                    Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}