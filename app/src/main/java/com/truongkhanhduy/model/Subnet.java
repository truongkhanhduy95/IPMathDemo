package com.truongkhanhduy.model;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Math.pow;

/**
 * Created by truongkhanhduy on 3/18/16.
 */
public class Subnet extends IP implements Serializable{
    private int[] broaddec;

    public Subnet()    //Standard constructor
    {
        super();
        broaddec = new int[5];
    }
    public Subnet(IP _subnet)       //Copy constructor
    {
        super(_subnet);
        broaddec = new int[5];
    }
    public void setSnm(int snm)
    {
        ipdec[4] = broaddec[4] = snm;
    }
    public void jumpStep(int js)
    {
        if (js < 256)		//Jump at Octet 4th
        {
            this.ipdec[3] += js;
            if (this.ipdec[3] > 255)
            {
                this.ipdec[3] = 0;
                this.ipdec[2] += 1;
                if (this.ipdec[2] > 255)
                {
                    this.ipdec[2] = 0;
                    this.ipdec[1] += 1;
                    if (this.ipdec[1] > 255)
                    {
                        this.ipdec[1] = 0;
                        this.ipdec[0] += 1;
                    }
                }
            }
        }	//end Jump 4th
        else
        if (js < 65536)	//Jump at Octet 3rd
        {
            this.ipdec[2] += (js / 256);
            if (this.ipdec[2] > 255)
            {
                this.ipdec[2] = 0;
                this.ipdec[1] += 1;
                if (this.ipdec[1] > 255)
                {
                    this.ipdec[1] = 0;
                    this.ipdec[0] += 1;
                }
            }
        }	//end Jump 3rd
        else
        if (js < 16777216)		//Jump at Octet 2nd
        {
            this.ipdec[1] += (js / 65536);
            if (this.ipdec[1] > 255)
            {
                this.ipdec[1] = 0;
                this.ipdec[0] += 1;
            }
        }	//end Jump 2nd
        else        //Jump at Octet 1st
        {
            this.ipdec[0] += (js / 16777216);
        }
    }
    public void broadcastSubnetBin()
    {
        for (int i = this.ipdec[4]; i < 32; i++)
            this.ipbin[i] = 1;
    }
    public String getBroadcastDec()
    {
        String str = "";
        for (int i = 0; i < 3; i++)
            str += broaddec[i] + ".";
        str += broaddec[3] + "/" + broaddec[4];
        return str;
    }
    public String broadcastSubnetDec()
    {
        this.broadcastSubnetBin();
        int S, exp, n = 0, first = 0, last = 0;
        do
        {
            S = exp = 0;
            for (last = first + 7; last >= first; last--)
                S+= this.ipbin[last] * (int)(pow((double) 2, (double) exp++));
            this.broaddec[n++] = S;
            first += 8;
        } while (first <= 24);
        this.broaddec[4] = ipdec[4];
        return this.getBroadcastDec();
    }

    public String usableHost()
    {
        String str = "";
        for (int i = 0; i < 3; i++)
            str += this.ipdec[i] + ".";
        str += (this.ipdec[3] + 1)+ " >> ";
        for (int i = 0; i < 3; i++)
            str += this.broaddec[i] + ".";
        str += (this.broaddec[3] - 1);
        return str;
    }

    public ArrayList<Subnet> getArrayOfUsableHost() throws InvalidException {
        try {
            ArrayList<Subnet> listSubnet = new ArrayList<>();
            int size = (this.broaddec[3] - 1) - (this.ipdec[3] + 1) + 1;
            for (int i = 0; i < size; i++) {
                String str = "";
                for (int j = 0; j < 3; j++)
                    str += this.ipdec[j] + ".";
                str += (this.ipdec[3] + 1 + i) + "/" + this.ipdec[4];
                IP ip = getIP(str);
                Subnet subnet = new Subnet(ip);

                listSubnet.add(subnet);
            }
            return listSubnet;
        } catch (InvalidException ex) {
            ex.printStackTrace();
            throw new InvalidException("Input invalid!");
        }
    }


}
