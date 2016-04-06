package com.truongkhanhduy.ipmathdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.truongkhanhduy.adapter.SubnetAdapter;
import com.truongkhanhduy.model.IP;
import com.truongkhanhduy.model.InvalidException;
import com.truongkhanhduy.model.Subnet;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.pow;

public class SubnetDetailActivity extends AppCompatActivity {

    private int host = 0;

    Intent myIntent;
    int js;
    IP ip;

    ListView lvSubnet;
    ArrayList<Subnet> listIP;
    SubnetAdapter adapterSubnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subnet_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subnet");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.subnet_theme));
        addControls();
        getData();
        DisplaySubnet();
        addEvents();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void addEvents() {
        lvSubnet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seeInfoSubnet(position);
            }
        });
    }

    private void seeInfoSubnet(int position) {
        myIntent=new Intent(
                SubnetDetailActivity.this,
                SubnetInfoActivity.class
        );
        Subnet sendSubnet= (Subnet) lvSubnet.getItemAtPosition(position);

        myIntent.putExtra("SUBNET_INFO",sendSubnet);
        startActivity(myIntent);
    }

    private void getData() {
        myIntent=getIntent();
        Bundle bundle=myIntent.getExtras();
        host=bundle.getInt("NUMBER_SUBNET");
        ip= (IP) bundle.getSerializable("IP");

    }

    private void addControls() {
        lvSubnet= (ListView) findViewById(R.id.lvSubnet);
        listIP=new ArrayList<>();
        adapterSubnet=new SubnetAdapter(
                SubnetDetailActivity.this,
                R.layout.item_subnet,
                listIP
        );
        lvSubnet.setAdapter(adapterSubnet);
    }

    private void DisplaySubnet() {

        listIP.clear();
        adapterSubnet.clear();
        Subnet subnet = new Subnet(ip.networkIP());

        js = 0;
        int newsnm = ip.getSnm();
        int[] result = noOfSubnet(newsnm,js);
        int noofsubnet=result[0];
        newsnm=result[1];
        js=result[2];

        subnet.setSnm(newsnm);
        subnet.broadcastSubnetBin();

        subnet.broadcastSubnetDec();
        listIP.add(subnet);
        Subnet lastedSubnet=subnet;
        for (int i = 2; i <= noofsubnet; i++)
        {
            Subnet newSubnet= new Subnet(lastedSubnet);
            newSubnet.jumpStep(js);
            newSubnet.decToBin();
            newSubnet.broadcastSubnetBin();
            newSubnet.broadcastSubnetDec();
            listIP.add(newSubnet);
            lastedSubnet=newSubnet;
        }
        adapterSubnet.notifyDataSetChanged();
    }

    private int[] noOfSubnet(int snm, int js) {
        int[] result=new int[3];
        int maxhost = 0;
        for (int i = 2; i < 33; i++) {
            maxhost = (int) pow(2, i) - 2;
            if (maxhost >= host) {
                js = maxhost + 2;
                int tsnm = snm;
                snm = 32 - i;
                result[0]=(int) pow(2, snm - tsnm);
                result[1]=snm;
                result[2]=js;
                return result;
            }
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item=menu.findItem(R.id.mnu_search);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSubnet(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()==0)
                    adapterSubnet.getFilter().filter("");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void searchSubnet(String query) {
        try {
            if (ValidateInsert(query)) {
                IP searchIP = IP.getIP(query);
                IP netIP = searchIP.networkIP();
                adapterSubnet.getFilter().filter(netIP.toString());
                //int pos=listIP.indexOf(netIP);
                //lvSubnet.setSelection(pos);
            }
        }
        catch (InvalidException e){
            Toast.makeText(SubnetDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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


}
