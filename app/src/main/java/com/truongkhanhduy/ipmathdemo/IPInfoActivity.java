package com.truongkhanhduy.ipmathdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.truongkhanhduy.model.IP;
import com.truongkhanhduy.model.InvalidException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPInfoActivity extends AppCompatActivity {

    Button btnDiv;
    TextView txtIP,txtIPBin,txtNetBin,txtNetDec,txtBroadBin,txtBroadDec,txtUsableHosts;

    IP myIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipinfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("IP Information");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.ipinfo_theme));

        addControls();
        getIPSend();
        addEvents();
    }

    private void getIPSend() {
        Intent intent = getIntent();
        if(intent.getExtras()!=null) {
            String strIP = intent.getStringExtra("INFO");
                try{
                    myIP=IP.getIP(strIP);
                    txtIP.setText(myIP.getIPDec());
                    getIPInfo();
                 }
                catch (InvalidException e){
                    Toast.makeText(IPInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void addEvents() {
        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPInfo();
            }
        });
    }

    private boolean ValidateInsert(String str)
    {
        Pattern pattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,3}");
        Matcher matcher=pattern.matcher(str);
        Pattern pattern2 = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher matcher2=pattern2.matcher(str);

        return matcher.matches()||matcher2.matches();
    }

    private void getIPInfo() {
        try {
            String ipInput = txtIP.getText().toString();
            ipInput=ipInput.replace(" ","");
            if (ValidateInsert(ipInput)) {
                myIP = new IP(ipInput);
                DisplayIPInfo(myIP);
            } else {
                Animation animation = AnimationUtils.loadAnimation(
                        IPInfoActivity.this,
                        R.anim.input_invalid
                );
                txtIP.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        txtIP.setTextColor(Color.RED);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        txtIP.setTextColor(Color.BLUE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
        catch (InvalidException e){
            Toast.makeText(IPInfoActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void DisplayIPInfo(IP myIP) {
        try {
            txtIPBin.setText(myIP.getIPBin());

            IP netIP = myIP.networkIP();
            txtNetBin.setText(netIP.getIPBin());
            txtNetDec.setText(netIP.getIPDec());


            IP broadcastIP = myIP.broadcastIP();
            txtBroadBin.setText(broadcastIP.getIPBin());
            txtBroadDec.setText(broadcastIP.getIPDec());

            txtUsableHosts.setText(myIP.usableHost(broadcastIP));
        }
        catch(Exception e){
            Toast.makeText(IPInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addControls() {
        btnDiv= (Button) findViewById(R.id.btnDiv);
        txtIP= (EditText) findViewById(R.id.txtIPInput);
        txtIPBin= (TextView) findViewById(R.id.txtIPBinary);
        txtNetBin= (TextView) findViewById(R.id.txtNetworkBinary);
        txtNetDec= (TextView) findViewById(R.id.txtNetworkDecimal);
        txtBroadBin= (TextView) findViewById(R.id.txtBroadcastBinary);
        txtBroadDec= (TextView) findViewById(R.id.txtBroadcastDecimal);
        txtUsableHosts= (TextView) findViewById(R.id.txtUsableHosts);
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

}
