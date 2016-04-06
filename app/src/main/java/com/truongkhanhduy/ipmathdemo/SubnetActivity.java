package com.truongkhanhduy.ipmathdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.truongkhanhduy.model.IP;
import com.truongkhanhduy.model.InvalidException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubnetActivity extends AppCompatActivity {

    Button btnDiv;
    EditText txtIPInput,txtNumberSubnet;
    Intent myIntent;

    int noSubnet;
    IP myIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subnet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subnet");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.subnet_theme));

        addControls();
        getIPSend();
        addEvents();
    }

    private void getIPSend() {
        myIntent = getIntent();
        if(myIntent.getExtras()!=null) {
            String strIP = myIntent.getStringExtra("SUBNET");
            txtIPInput.setText(strIP);
        }
    }

    private boolean getData() {
        try {
            String str = txtIPInput.getText().toString();
            str=str.replace(" ","");

            if (!ValidateInsert(str)) {
                Animation animation = AnimationUtils.loadAnimation(
                        SubnetActivity.this,
                        R.anim.input_invalid
                );
                txtIPInput.startAnimation(animation);
                return false;
            }
            myIP = new IP(str);
            noSubnet = Integer.parseInt(txtNumberSubnet.getText().toString());
            return true;
        }
        catch (InvalidException e){
            Toast.makeText(SubnetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean ValidateInsert(String str)
    {
        Pattern pattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,3}");
        Matcher matcher=pattern.matcher(str);
        Pattern pattern2 = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher matcher2=pattern2.matcher(str);

        return matcher.matches()||matcher2.matches();
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
                sendData();
            }
        });
    }

    private void addControls() {
        txtIPInput= (EditText) findViewById(R.id.txtIPInput);
        txtNumberSubnet= (EditText) findViewById(R.id.txtNumberSubnet);
        btnDiv= (Button) findViewById(R.id.btnDiv);
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


    private void sendData() {
        if(getData()) {
            myIntent = new Intent(
                    SubnetActivity.this,
                    SubnetDetailActivity.class
            );
            Bundle bundle = new Bundle();
            bundle.putInt("NUMBER_SUBNET", noSubnet);
            bundle.putSerializable("IP", myIP);
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }
    }
}
