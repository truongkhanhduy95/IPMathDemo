package com.truongkhanhduy.ipmathdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btnIPInfo,btnSubnet,btnConvert,btnCamera;
    Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnIPInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent=new Intent(
                        MainActivity.this,
                        IPInfoActivity.class
                );
                startActivity(myIntent);
            }
        });
        btnSubnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent=new Intent(
                        MainActivity.this,
                        SubnetActivity.class
                );
                startActivity(myIntent);
            }
        });
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent=new Intent(
                        MainActivity.this,
                        ConvertIPv6Activity.class
                );
                startActivity(myIntent);
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent=new Intent(
                        MainActivity.this,
                        CameraActivity.class
                );
                startActivity(myIntent);
            }
        });
    }

    private void addControls() {
        btnIPInfo= (ImageButton) findViewById(R.id.btnIPInfo);
        btnSubnet= (ImageButton) findViewById(R.id.btnSubnet);
        btnConvert= (ImageButton) findViewById(R.id.btnConvert);
        btnCamera= (ImageButton) findViewById(R.id.btnCamera);
    }
}
