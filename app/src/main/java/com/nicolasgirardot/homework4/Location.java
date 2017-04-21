package com.nicolasgirardot.homework4;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nicolasgirardot on 4/20/17.
 */

public class Location {

    private int id;
    private LatLng position;
    private float zoom;

    public Location() {
    }

    public Location(LatLng position, float zoom) {
        this.position = position;
        this.zoom = zoom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
