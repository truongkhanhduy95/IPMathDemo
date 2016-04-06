package com.truongkhanhduy.ipmathdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.truongkhanhduy.model.IP;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertIPv6Activity extends AppCompatActivity {


    
    Button btnConvert;
    EditText txtInput;
    TextView txtIPv6,txtIPv4;

    String strInput,ipv6;
    Intent myIntent;
    IP myIP;

    String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_ipv6);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("IPv6 Converter");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.ipv6_theme));
        addControls();
        getData();
        addEvents();
    }

    private void getData() {
        myIntent = getIntent();
        if(myIntent.getExtras()!=null) {
            String strIP = myIntent.getStringExtra("SUBNET");
            txtInput.setText(strIP);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void addEvents() {
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToIPv6();
            }
        });
    }

    private void convertToIPv6() {
        strInput = txtInput.getText().toString();
        ipv6="";
        getIPv6(strInput);
        if(!ipv6.equals("")) {
            txtIPv6.setText(ipv6);
            txtIPv4.setText(strInput);
        }
    }

    private void addControls() {
        btnConvert= (Button) findViewById(R.id.btnConvert);
        txtInput= (EditText) findViewById(R.id.txtInput);
        txtIPv6= (TextView) findViewById(R.id.txtIPv6);
        txtIPv4= (TextView) findViewById(R.id.txtIPv4);
        ImageView btnReset= (ImageView) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }
    private boolean ValidateInsert(String str)
    {
        Pattern pattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher matcher=pattern.matcher(str);

        return matcher.matches();
    }

    public void getIPv6(String ipv4Str){
        if(ValidateInsert(strInput)) {
            String[] octecString = ipv4Str.split("\\.");
            int[] octec = new int[4];
            for (int i = 0; i < 4; i++) {
                octec[i] = Integer.parseInt(octecString[i]);
            }

            for (int i = 0; i < 4; i++) {
                String times = hex[octec[i] / 16];
                String left = hex[octec[i] % 16];
                ipv6 += times + left;
                if (i == 1)
                    ipv6 += ":";
            }
            ipv6 = "::" + ipv6;
        }
        else{
            Toast.makeText(ConvertIPv6Activity.this, "Input invalid!", Toast.LENGTH_SHORT).show();
        }
    }
}
