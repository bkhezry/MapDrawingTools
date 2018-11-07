package com.github.bkhezry.mapdrawingtools.model;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.github.bkhezry.mapdrawingtools.ui.MapsActivity;


public class DrawingOptionBuilder {
  private Double locationLatitude;
  private Double locationLongitude;
  private float zoom = 14;
  private int fillColor = Color.argb(0, 0, 0, 0);
  private int strokeColor = Color.argb(255, 0, 0, 0);
  private int strokeWidth = 10;
  private Boolean enableSatelliteView = true;
  private Boolean requestGPSEnabling = false;
  private Boolean enableCalculateLayout = true;
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

  public DrawingOptionBuilder withRequestGPSEnabling(Boolean requestGPSEnabling) {
    this.requestGPSEnabling = requestGPSEnabling;
    return this;
  }

  public DrawingOptionBuilder withMapZoom(float zoom) {
    this.zoom = zoom;
    return this;
  }

  public DrawingOptionBuilder withCalculateLayoutHidden() {
    this.enableCalculateLayout = false;
    return this;
  }

  public Intent build(Context context) {
    if (drawingType == DrawingOption.DrawingType.POINT)
      enableCalculateLayout = false;
    Intent intent = new Intent(context, MapsActivity.class);
    DrawingOption drawingOption = new DrawingOption(locationLatitude, locationLongitude, zoom, fillColor, strokeColor, strokeWidth, enableSatelliteView, requestGPSEnabling, enableCalculateLayout, drawingType);
    intent.putExtra(MapsActivity.MAP_OPTION, drawingOption);
    return intent;
  }
}
