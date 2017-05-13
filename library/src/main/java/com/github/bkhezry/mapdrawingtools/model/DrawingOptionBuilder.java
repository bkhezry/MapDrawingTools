package com.github.bkhezry.mapdrawingtools.model;


import android.content.Context;
import android.content.Intent;


import com.github.bkhezry.mapdrawingtools.ui.MapsActivity;


public class DrawingOptionBuilder {
    private Double locationLatitude;
    private Double locationLongitude;
    private int fillColor;
    private int strokeColor;
    private int strokeWidth;
    private boolean enableSatelliteView = true;
    private DrawingOption.DrawingType drawingType = DrawingOption.DrawingType.POLYGON;

    public DrawingOptionBuilder() {
    }

    public DrawingOptionBuilder withLocation(double latitude, double longitude) {
        this.locationLatitude = latitude;
        this.locationLongitude = longitude;
        return this;
    }

    public DrawingOptionBuilder withSatelliteViewHidden() {
        this.enableSatelliteView = false;
        return this;
    }

    public DrawingOptionBuilder withDrawingType(DrawingOption.DrawingType drawingType) {
        this.drawingType = drawingType;
        return this;
    }

    public DrawingOptionBuilder withFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public DrawingOptionBuilder withStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public DrawingOptionBuilder withStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public Intent build(Context context) {
        Intent intent = new Intent(context, MapsActivity.class);
        DrawingOption drawingOption = new DrawingOption(locationLatitude, locationLongitude, fillColor, strokeColor, strokeWidth, enableSatelliteView, drawingType);
        intent.putExtra(MapsActivity.MAP_OPTION, drawingOption);
        return intent;
    }
}
