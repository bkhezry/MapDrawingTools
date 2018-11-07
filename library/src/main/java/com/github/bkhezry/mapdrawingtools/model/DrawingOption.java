package com.github.bkhezry.mapdrawingtools.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DrawingOption implements Parcelable {
  public static final Creator<DrawingOption> CREATOR = new Creator<DrawingOption>() {
    @Override
    public DrawingOption createFromParcel(Parcel source) {
      return new DrawingOption(source);
    }

    @Override
    public DrawingOption[] newArray(int size) {
      return new DrawingOption[size];
    }
  };
  private Double locationLatitude;
  private Double locationLongitude;
  private float zoom;
  private int fillColor;
  private int strokeColor;
  private int strokeWidth;
  private Boolean enableSatelliteView;
  private Boolean requestGPSEnabling;
  private Boolean enableCalculateLayout;
  private DrawingOption.DrawingType drawingType;

  public DrawingOption(Double locationLatitude, Double locationLongitude, float zoom, int fillColor, int strokeColor, int strokeWidth, Boolean enableSatelliteView, Boolean requestGPSEnabling, Boolean enableCalculateLayout, DrawingType drawingType) {
    this.locationLatitude = locationLatitude;
    this.locationLongitude = locationLongitude;
    this.zoom = zoom;
    this.fillColor = fillColor;
    this.strokeColor = strokeColor;
    this.strokeWidth = strokeWidth;
    this.enableSatelliteView = enableSatelliteView;
    this.requestGPSEnabling = requestGPSEnabling;
    this.enableCalculateLayout = enableCalculateLayout;
    this.drawingType = drawingType;
  }

  protected DrawingOption(Parcel in) {
    this.locationLatitude = (Double) in.readValue(Double.class.getClassLoader());
    this.locationLongitude = (Double) in.readValue(Double.class.getClassLoader());
    this.zoom = in.readFloat();
    this.fillColor = in.readInt();
    this.strokeColor = in.readInt();
    this.strokeWidth = in.readInt();
    this.enableSatelliteView = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.requestGPSEnabling = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.enableCalculateLayout = (Boolean) in.readValue(Boolean.class.getClassLoader());
    int tmpDrawingType = in.readInt();
    this.drawingType = tmpDrawingType == -1 ? null : DrawingType.values()[tmpDrawingType];
  }

  public Double getLocationLatitude() {
    return locationLatitude;
  }

  public void setLocationLatitude(Double locationLatitude) {
    this.locationLatitude = locationLatitude;
  }

  public Double getLocationLongitude() {
    return locationLongitude;
  }

  public void setLocationLongitude(Double locationLongitude) {
    this.locationLongitude = locationLongitude;
  }

  public float getZoom() {
    return zoom;
  }

  public void setZoom(float zoom) {
    this.zoom = zoom;
  }

  public int getFillColor() {
    return fillColor;
  }

  public void setFillColor(int fillColor) {
    this.fillColor = fillColor;
  }

  public int getStrokeColor() {
    return strokeColor;
  }

  public void setStrokeColor(int strokeColor) {
    this.strokeColor = strokeColor;
  }

  public int getStrokeWidth() {
    return strokeWidth;
  }

  public void setStrokeWidth(int strokeWidth) {
    this.strokeWidth = strokeWidth;
  }

  public Boolean getEnableSatelliteView() {
    return enableSatelliteView;
  }

  public void setEnableSatelliteView(Boolean enableSatelliteView) {
    this.enableSatelliteView = enableSatelliteView;
  }

  public Boolean getRequestGPSEnabling() {
    return requestGPSEnabling;
  }

  public void setRequestGPSEnabling(Boolean requestGPSEnabling) {
    this.requestGPSEnabling = requestGPSEnabling;
  }

  public Boolean getEnableCalculateLayout() {
    return enableCalculateLayout;
  }

  public void setEnableCalculateLayout(Boolean enableCalculateLayout) {
    this.enableCalculateLayout = enableCalculateLayout;
  }

  public DrawingType getDrawingType() {
    return drawingType;
  }

  public void setDrawingType(DrawingType drawingType) {
    this.drawingType = drawingType;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.locationLatitude);
    dest.writeValue(this.locationLongitude);
    dest.writeFloat(this.zoom);
    dest.writeInt(this.fillColor);
    dest.writeInt(this.strokeColor);
    dest.writeInt(this.strokeWidth);
    dest.writeValue(this.enableSatelliteView);
    dest.writeValue(this.requestGPSEnabling);
    dest.writeValue(this.enableCalculateLayout);
    dest.writeInt(this.drawingType == null ? -1 : this.drawingType.ordinal());
  }

  public enum DrawingType {
    POLYGON, POLYLINE, POINT
  }
}
