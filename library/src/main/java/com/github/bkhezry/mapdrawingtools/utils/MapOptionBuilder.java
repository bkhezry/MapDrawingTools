package com.github.bkhezry.mapdrawingtools.utils;


import android.content.Context;
import android.content.Intent;

import com.github.bkhezry.mapdrawingtools.ui.MapsActivity;


public class MapOptionBuilder {
    public static final String LATITUDE = "locationLatitude";
    public static final String LONGITUDE = "locationLongitude";
    public static final String ENABLE_SATELLITE_VIEW = "enable_satellite_view";
    public static final String DRAWING_TYPE = "drawing_type";
    private Double locationLatitude;
    private Double locationLongitude;
    private boolean enableSatelliteView = true;
    private DrawingType drawingType = DrawingType.POLYGON;

    public enum DrawingType {
        POLYGON, POLYLINE, POINT
    }

    public MapOptionBuilder() {
    }

    public MapOptionBuilder withLocation(double latitude, double longitude) {
        this.locationLatitude = latitude;
        this.locationLongitude = longitude;
        return this;
    }

    public MapOptionBuilder withSatelliteViewHidden() {
        this.enableSatelliteView = false;
        return this;
    }

    public MapOptionBuilder withDrawingType(DrawingType drawingType) {
        this.drawingType = drawingType;
        return this;
    }

    public Intent build(Context context) {
        Intent intent = new Intent(context, MapsActivity.class);

        if (locationLatitude != null) {
            intent.putExtra(LATITUDE, locationLatitude);
        }
        if (locationLongitude != null) {
            intent.putExtra(LONGITUDE, locationLongitude);
        }

        intent.putExtra(ENABLE_SATELLITE_VIEW, enableSatelliteView);
        intent.putExtra(DRAWING_TYPE, drawingType);
        return intent;
    }
}
