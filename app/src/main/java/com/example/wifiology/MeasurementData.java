package com.example.wifiology;

import java.util.Date;

public class MeasurementData implements Comparable<MeasurementData>{

    private Date time;
    private int mgntFrames;
    private int dataFrames;
    private int cntlFrames;
    private int throughput;
    private int stations;

    public MeasurementData(Date t, int m, int d, int c, int th, int s){
        time = t;
        mgntFrames = m;
        dataFrames = d;
        cntlFrames = c;
        throughput = th;
        stations = s;
    }

    public void addData(int m, int d, int c, int th, int s){
        mgntFrames += m;
        dataFrames += d;
        cntlFrames += c;
        throughput += th;
        stations += s;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getMgntFrames() {
        return mgntFrames;
    }

    public void setMgntFrames(int mgntFrames) {
        this.mgntFrames = mgntFrames;
    }

    public int getDataFrames() {
        return dataFrames;
    }

    public void setDataFrames(int dataFrames) {
        this.dataFrames = dataFrames;
    }

    public int getCntlFrames() {
        return cntlFrames;
    }

    public void setCntlFrames(int cntlFrames) {
        this.cntlFrames = cntlFrames;
    }

    public int getThroughput() {
        return throughput;
    }

    public void setThroughput(int throughput) {
        this.throughput = throughput;
    }

    public int getStations() {
        return stations;
    }

    public void setStations(int stations) {
        this.stations = stations;
    }

    @Override
    public int compareTo(MeasurementData data) {
        return time.compareTo(data.getTime());

    }
}
