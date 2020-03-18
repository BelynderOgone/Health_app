package com.example.myapplication.models;

import java.util.List;

public class hospitals {
    private String hId;
    private String name;
    private Double lat;
    private Double longi;
    private String kephLevel;
    private List<ServicesModel> servicesOfferedList;

    public hospitals() {
    }

    public hospitals(String mHId, String mName, Double mLat, Double mLongi, String mKephLevel, List<ServicesModel> mServicesCountList) {
        this.hId = mHId;
        this.name = mName;
        this.lat = mLat;
        this.longi = mLongi;
        this.kephLevel = mKephLevel;
        this.servicesOfferedList = mServicesCountList;
    }

    public List<ServicesModel> getServicesOfferedList() {
        return servicesOfferedList;
    }

    public String gethId() {
        return hId;
    }

    public String getName() {
        return name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLongi() {
        return longi;
    }

    public String getKephLevel() {
        return kephLevel;
    }

    public void setServicesOfferedList(List<ServicesModel> servicesOfferedList) {
        this.servicesOfferedList = servicesOfferedList;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public void setKephLevel(String kephLevel) {
        this.kephLevel = kephLevel;
    }
}