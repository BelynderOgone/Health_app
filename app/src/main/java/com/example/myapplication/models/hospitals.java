package com.example.myapplication;

public class hospitals {
    private String hId;
    private String name;
    private String lat;
    private String longi;
    private String kephLevel;

    public hospitals() {
    }

    public hospitals(String mHId, String mName, String mLat, String mLongi, String mKephLevel) {
        this.hId = mHId;
        this.name = mName;
        this.lat = mLat;
        this.longi = mLongi;

        this.kephLevel = mKephLevel;
    }

    public String gethId() {
        return hId;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLongi() {
        return longi;
    }

    public String getOtherS() {
        return kephLevel;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public void setOtherS(String kephLevel) {
        this.kephLevel = kephLevel;
    }
}