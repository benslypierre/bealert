package com.example.bealert.models;

import com.google.android.gms.maps.model.LatLng;

public class Favourites {
    LatLng location;
    String name;

    public Favourites(String name, LatLng location) {
        this.location = location;
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
