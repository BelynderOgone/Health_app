package com.example.myapplication.models;

public class hospitals {
    private String hId;
    private String name;
    private String lat;
    private String longi;
    private String kephLevel;
    private String servicesOfferedCount;

    public hospitals() {
    }

    public hospitals(String mHId, String mName, String mLat, String mLongi, String mKephLevel, String mServicesCountLevel) {
        this.hId = mHId;
        this.name = mName;
        this.lat = mLat;
        this.longi = mLongi;

        this.kephLevel = mKephLevel;
    }

    public String getServicesOfferedCount() {
        return servicesOfferedCount;
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

    public String getKephLevel() {
        return kephLevel;
    }

    public void setServicesOfferedCount(String servicesOfferedCount) {
        this.servicesOfferedCount = servicesOfferedCount;
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

    public void setKephLevel(String kephLevel) {
        this.kephLevel = kephLevel;
    }
}