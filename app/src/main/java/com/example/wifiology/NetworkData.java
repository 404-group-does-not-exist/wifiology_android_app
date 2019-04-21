package com.example.wifiology;

import java.util.Date;

public class NetworkData {

    private String ssid;
    private boolean expanded;
    private Date earliestTime;
    private Date latestTime;

    public NetworkData(String _ssid){
        ssid = _ssid;
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

    public void setEarliestTime(Date d){
        earliestTime = d;
    }

    public void setLatestTime(Date d){
        latestTime = d;
    }

    public Date getEarliestTime(){
        return earliestTime;
    }

    public Date getLatestTime(){
        return latestTime;
    }

}
