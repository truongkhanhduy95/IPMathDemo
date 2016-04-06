package com.truongkhanhduy.model;

import java.io.Serializable;

import static java.lang.Math.pow;

/**
 * Created by truongkhanhduy on 3/17/16.
 */
public class IP implements Serializable{
    public int[] ipdec, ipbin;
    public IP()                //Standard constructor
    {
        ipdec = new int[5];
        ipbin = new int[32];
    }
    public IP(IP _ip)           //Copy constructor
    {
        ipdec = new int[5];
        ipbin = new int[32];
        for (int i = 0; i < 5; i++)
            this.ipdec[i] = _ip.ipdec[i];
        for (int i = 0; i < 32; i++)
            this.ipbin[i] = _ip.ipbin[i];
    }

    public IP(String ipstring) throws InvalidException {
        ipdec = new int[5];
        String iptemp = ipstring;
        ipdec[0] = Integer.parseInt((iptemp.split("[.]")[0].trim()));
        ipdec[1] = Integer.parseInt((iptemp.split("[.]")[1].trim()));
        ipdec[2] = Integer.parseInt((iptemp.split("[.]")[2].trim()));

        if(ipstring.contains("/")){
            iptemp = iptemp.split("[.]")[3];
            ipdec[3] = Integer.parseInt((iptemp.split("[/]")[0].trim()));
            ipdec[4] = Integer.parseInt((iptemp.split("[/]")[1].trim()));
        }
        else{
            ipdec[3] = Integer.parseInt((iptemp.split("[.]")[3].trim()));
            if(ipdec[0]<=126)
                ipdec[4]=8;
            else if(ipdec[0]>127&&ipdec[0]<=191)
                ipdec[4]=16;
            else if(ipdec[0]>=192&&ipdec[0]<=223)
                ipdec[4]=24;
            else
                throw new InvalidException("IP does not match!");
        }


        if(ipdec[4]<0||ipdec[4]>32)
            throw new InvalidException("Prefix invalid!");
        for(int i = 0;i<4;i++){
            if(ipdec[i]<0||ipdec[i]>255){
                throw new InvalidException("Octec invalid!");
            }
        }

        decToBin();
    }
    public void setPrefix(int prefix){
        this.ipdec[4]=prefix;
    }

    public void setIPDec(int[] _ipdec)
    {
        for (int i = 0; i < 5; i++)
            ipdec[i] = _ipdec[i];
    }
    public void setIPBin(int[] _ipbin)
    {
        for (int i = 0; i < 32; i++)
            this.ipbin[i] = _ipbin[i];
    }
    public void setSnm(int snm)
    {
        this.ipdec[4] = snm;
    }

    public int getSnm(){
        return ipdec[4];
    }
    public String getIPDec()
    {
        String str ="";
        for (int i = 0; i < 3; i++)
            str += ipdec[i] + ".";
        str += ipdec[3] + " /" + ipdec[4];
        return str;
    }
    public String getIPBin()
    {
        String str = "";
        for (int i = 0; i < 32; i++)
        {
            if (i % 8 == 0)
                str += " ";
            str += ipbin[i]+"";
        }
        str=str.trim();
        return str;
    }
    public void getIPBinArray( int[] iparray)
    {
        for (int i = 0; i < 32; i++)
            iparray[i] = this.ipbin[i];
    }
    public void decToBin()
    {
        ipbin=new int[32];
        int n = 0;
        int[] _ipbin = new int[8];
        int[] _ipdec = new int[4];
        for (int i = 0; i < 4; i++)
            _ipdec[i] = ipdec[i];

        for (int i = 0; i < 4; i++)
        {
            for (int t = 0; t < 8; t++) _ipbin[t] = 0;
            int j = 7;
            do
            {
                _ipbin[j--] = _ipdec[i] % 2;
                _ipdec[i] /= 2;
            } while (_ipdec[i] != 0);

            for (int t = 0; t < 8; t++)
                ipbin[n++] = _ipbin[t];
        }
    }
    public void binToDec()
    {
        ipdec=new int[5];
        int S, exp, n = 0, first = 0, last = 0;
        do
        {
            S = exp = 0;
            for (last = first + 7; last >= first; last--)
                S += ipbin[last] * (int)(pow((double) 2, (double) exp++));
            ipdec[n++] = S;
            first += 8;
        } while (first <= 24);
    }
    public IP networkIP()
    {
        decToBin();
        IP netIP=new IP();
        int[] dectemp=new int[5];
        int[] bintemp=new int[32];
        for (int i=0;i<5;i++)
            dectemp[i]=this.ipdec[i];
        for (int i = 0; i < 32; i++)
            bintemp[i] = this.ipbin[i];
        for (int i = dectemp[4]; i < 32; i++)
            bintemp[i] = 0;
        netIP.setIPBin(bintemp);
        netIP.binToDec();
        netIP.setPrefix(dectemp[4]);
        return netIP;
    }

    public IP broadcastIP()
    {
        IP broadIP=new IP();
        decToBin();
        int[] dectemp=new int[5];
        int[] bintemp=new int[32];
        for (int i=0;i<5;i++)
            dectemp[i]=this.ipdec[i];
        for (int i = 0; i < 32; i++)
           bintemp[i] = this.ipbin[i];
        for (int i = dectemp[4]; i < 32; i++)
            bintemp[i] = 1;
        broadIP.setIPBin(bintemp);
        broadIP.binToDec();
        broadIP.setPrefix(dectemp[4]);
        return broadIP;
    }

    public String usableHost(IP last)
    {
        String str = "";
        for (int i = 0; i < 3; i++)
            str += this.ipdec[i] + ".";
        str += (this.ipdec[3] + 1) + " >> ";
        for (int i = 0; i < 3; i++)
            str += last.ipdec[i] + ".";
        str += (last.ipdec[3] - 1)+"";
        return str;
    }

    public static IP getIP(String ipstring) throws InvalidException {
        int[] octectemp = new int[5];
        String iptemp = ipstring;
        octectemp[0] = Integer.parseInt((iptemp.split("[.]")[0].trim()));
        octectemp[1] = Integer.parseInt((iptemp.split("[.]")[1].trim()));
        octectemp[2] = Integer.parseInt((iptemp.split("[.]")[2].trim()));

        if(ipstring.contains("/")){
            iptemp = iptemp.split("[.]")[3];
            octectemp[3] = Integer.parseInt((iptemp.split("[/]")[0].trim()));
            octectemp[4] = Integer.parseInt((iptemp.split("[/]")[1].trim()));
        }
        else{
            octectemp[3] = Integer.parseInt((iptemp.split("[.]")[3].trim()));
            if(octectemp[0]<=126)
                octectemp[4]=8;
            else if(octectemp[0]>=127&&octectemp[0]<=191)
                octectemp[4]=16;
            else if(octectemp[0]>=192&&octectemp[0]<=223)
                octectemp[4]=24;
            else
                throw new InvalidException("IP does not match!");
        }

        if(octectemp[4]<0||octectemp[4]>32)
            throw new InvalidException("Prefix invalid!");
        for(int i = 0;i<4;i++){
            if(octectemp[i]<0||octectemp[i]>255){
                throw new InvalidException("Input invalid!");
            }
        }

        IP myIP = new IP();
        myIP.setIPDec(octectemp);


        return myIP;
    }

    @Override
    public String toString() {
        return getIPDec();
    }
}
