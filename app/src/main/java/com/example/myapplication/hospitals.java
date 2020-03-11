package com.example.myapplication;

public class hospitals {
    public String hId;
    public String name;
    public String lat;
    public String longi;
    public String mains1;
    public String mains2;
    public String mains3;
    public String main4;
    public String otherS;

    public hospitals() {
    }

    public hospitals(String hId, String name, String lat, String longi, String mains1, String mains2, String mains3, String main4, String otherS) {
        this.hId = hId;
        this.name = name;
        this.lat = lat;
        this.longi = longi;
        this.mains1 = mains1;
        this.mains2 = mains2;
        this.mains3 = mains3;
        this.main4 = main4;
        this.otherS = otherS;
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

    public String getMains1() {
        return mains1;
    }

    public String getMains2() {
        return mains2;
    }

    public String getMains3() {
        return mains3;
    }

    public String getMain4() {
        return main4;
    }

    public String getOtherS() {
        return otherS;
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

    public void setMains1(String mains1) {
        this.mains1 = mains1;
    }

    public void setMains2(String mains2) {
        this.mains2 = mains2;
    }

    public void setMains3(String mains3) {
        this.mains3 = mains3;
    }

    public void setMain4(String main4) {
        this.main4 = main4;
    }

    public void setOtherS(String otherS) {
        this.otherS = otherS;
    }
}