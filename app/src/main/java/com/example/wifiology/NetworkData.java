package com.example.wifiology;

public class NetworkData {

    private String ssid;
    private boolean expanded;

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

}
