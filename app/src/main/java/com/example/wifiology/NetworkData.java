package com.example.wifiology;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkData implements Comparable<NetworkData>{

    private String ssid;
    private boolean expanded;
    private float mngtFrameRatio = -24f;
    ArrayList<String> bssids;

    public NetworkData(String _ssid){
        ssid = _ssid;
        bssids = new ArrayList<String>();
    }

    public String getSsid(){
        return ssid;
    }

    public boolean getExpanded(){
        return expanded;
    }

    public void setExpanded(boolean _expanded){
        expanded = _expanded;
    }

    public void addBssid(String id) {
        bssids.add(id);
    }

    public ArrayList<String> getBssids(){
        return bssids;
    }

    public float getMngtFrameRatio() {
        return mngtFrameRatio;
    }

    public void setMngtFrameRatio(float mngtFrameRatio) {
        this.mngtFrameRatio = mngtFrameRatio;
    }

    @Override
    public int compareTo(NetworkData data) {
        return ssid.compareTo(data.getSsid());

    }

}
