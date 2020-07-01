package com.example.planmytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation topAnim,bottomAnim;
    TextView plan,trip,slogan,loading;
    ImageView imageLogo;

    public static int SPLASH_SCREEN=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        plan = findViewById(R.id.txtPlan);
        trip = findViewById(R.id.txtTrip);
        slogan = findViewById(R.id.txtSlogan);
        loading = findViewById(R.id.txtLoad);
        imageLogo = findViewById(R.id.ImageLogo);

        plan.setAnimation(topAnim);
        imageLogo.setAnimation(topAnim);
        trip.setAnimation(topAnim);
        loading.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish();
            }
        },SPLASH_SCREEN);


    }
}