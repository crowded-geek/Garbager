package com.aayushman.garbager.model;

public class Putting {
    String photourl;
    String time;
    String date;
    String lon;
    String lat;

    public Putting(String photourl, String time, String date, String lon, String lat) {
        this.photourl = photourl;
        this.time = time;
        this.date = date;
        this.lon = lon;
        this.lat = lat;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
