package com.truongkhanhduy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.truongkhanhduy.ipmathdemo.R;
import com.truongkhanhduy.model.Subnet;

import java.util.List;

/**
 * Created by truongkhanhduy on 3/18/16.
 */
public class SubnetAdapter extends ArrayAdapter<Subnet> {
    Activity context;
    int resource;
    List<Subnet> objects;

    public SubnetAdapter(Activity context, int resource, List<Subnet> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View view=inflater.inflate(this.resource, null);

        TextView txtNet= (TextView) view.findViewById(R.id.txtNet);
        TextView txtBroad= (TextView) view.findViewById(R.id.txtBroad);
        TextView txtSTT= (TextView) view.findViewById(R.id.txtSTT);

        Subnet subnet = getItem(position);
        txtNet.setText(subnet.getIPDec());
        txtBroad.setText(subnet.getBroadcastDec());
        txtSTT.setText(position+1+"");
        return view;
    }

    @Override
    public Subnet getItem(int position)
    {
        return super.getItem(position);
    }

    @Override
    public int getPosition(Subnet item)
    {
        return objects.indexOf(item);
    }

}
