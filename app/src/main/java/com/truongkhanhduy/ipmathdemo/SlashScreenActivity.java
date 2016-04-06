package com.truongkhanhduy.ipmathdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SlashScreenActivity extends AppCompatActivity {

    ImageView imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slash_screen);
        addControls();
        slashScreen();
    }

    private void slashScreen() {
        Animation rotate_logo= AnimationUtils.loadAnimation(
                SlashScreenActivity.this,
                R.anim.slash_rotate
        );
        imgLogo.startAnimation(rotate_logo);
        rotate_logo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent intent=new Intent(
                        SlashScreenActivity.this,
                        MainActivity.class
                );
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addControls() {
        imgLogo= (ImageView) findViewById(R.id.imgLogo);
    }
}
