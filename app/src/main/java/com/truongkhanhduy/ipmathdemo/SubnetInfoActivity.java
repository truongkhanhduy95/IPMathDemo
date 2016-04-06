package com.truongkhanhduy.ipmathdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.truongkhanhduy.adapter.SubnetInfoAdapter;
import com.truongkhanhduy.model.IP;
import com.truongkhanhduy.model.InvalidException;
import com.truongkhanhduy.model.Subnet;

import java.util.ArrayList;

public class SubnetInfoActivity extends AppCompatActivity {

    Intent myIntent;
    Subnet subnet;
    ListView lvSubnetInfo;
    ArrayList<IP> listSubnet;
    SubnetInfoAdapter adapterSubnetInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subnet_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subnet");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.subnet_theme));
        addControls();
        disPlaySubnetInfo();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void disPlaySubnetInfo() {
        try{
            IP ip=IP.getIP(subnet.getIPDec());
            listSubnet.add(ip);
            ArrayList<Subnet> listHost=subnet.getArrayOfUsableHost();
            for (int i=0;i<listHost.size();i++) {
                Subnet subnet = listHost.get(i);
                listSubnet.add(subnet);
            }
            IP broadIP=IP.getIP(subnet.broadcastSubnetDec());
            listSubnet.add(broadIP);

            adapterSubnetInfo.notifyDataSetChanged();
        }
        catch (InvalidException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void addControls() {
        myIntent=getIntent();
        subnet= (Subnet) myIntent.getSerializableExtra("SUBNET_INFO");

        lvSubnetInfo= (ListView) findViewById(R.id.lvSubnetInfo);
        listSubnet=new ArrayList<>();
        adapterSubnetInfo=new SubnetInfoAdapter(
                SubnetInfoActivity.this,
                R.layout.item_ip,
                listSubnet
        );
        lvSubnetInfo.setAdapter(adapterSubnetInfo);
        //Toast.makeText(SubnetInfoActivity.this, subnet.getIPDec(), Toast.LENGTH_SHORT).show();
    }
}
