package com.truongkhanhduy.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.truongkhanhduy.ipmathdemo.R;
import com.truongkhanhduy.model.IP;

import java.util.List;

/**
 * Created by truongkhanhduy on 3/18/16.
 */
public class SubnetInfoAdapter extends ArrayAdapter<IP> implements Filterable{
    Activity context; int resource; List<IP> objects;
    public SubnetInfoAdapter(Activity context, int resource, List<IP> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource, null);

        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);

        IP myiP = this.objects.get(position);

        //IP tempIP=IP.getIP(myiP.getIPDec());
        IP netIP = myiP.networkIP();
        IP broadIP = myiP.broadcastIP();
        if (myiP.getIPBin().equals(netIP.getIPBin())) {
            txtName.setText("Network");
            txtName.setTextColor(Color.RED);
            txtNumber.setTextColor(Color.RED);
        } else if (myiP.getIPBin().equals(broadIP.getIPBin())){
            txtName.setText("Broadcast");
            txtName.setTextColor(Color.MAGENTA);
            txtNumber.setTextColor(Color.MAGENTA);
        }
        else
            txtName.setText("Host");
        txtNumber.setText(myiP.getIPDec().toString());

        Animation animation= AnimationUtils.loadAnimation((Context) this.context, R.anim.slide_right_in);
        view.startAnimation(animation);

        return view;
    }


}
