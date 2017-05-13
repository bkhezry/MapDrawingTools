package com.github.bkhezry.mapdrawingtools.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class DrawingOption implements Parcelable {
    private Double locationLatitude;
    private Double locationLongitude;
    private int fillColor = Color.argb(0, 0, 0, 0);
    private int strokeColor = Color.argb(255, 0, 0, 0);
    private int strokeWidth = 10;
    private boolean enableSatelliteView = true;
    private DrawingType drawingType = DrawingType.POLYGON;
    public enum DrawingType {
        POLYGON, POLYLINE, POINT
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

    public boolean isEnableSatelliteView() {
        return enableSatelliteView;
    }

    public void setEnableSatelliteView(boolean enableSatelliteView) {
        this.enableSatelliteView = enableSatelliteView;
    }

    public DrawingType getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(DrawingType drawingType) {
        this.drawingType = drawingType;
    }

    public DrawingOption(Double locationLatitude, Double locationLongitude, int fillColor, int strokeColor, int strokeWidth, boolean enableSatelliteView, DrawingType drawingType) {
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.enableSatelliteView = enableSatelliteView;
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
        dest.writeInt(this.fillColor);
        dest.writeInt(this.strokeColor);
        dest.writeInt(this.strokeWidth);
        dest.writeByte(this.enableSatelliteView ? (byte) 1 : (byte) 0);
        dest.writeInt(this.drawingType == null ? -1 : this.drawingType.ordinal());
    }

    protected DrawingOption(Parcel in) {
        this.locationLatitude = (Double) in.readValue(Double.class.getClassLoader());
        this.locationLongitude = (Double) in.readValue(Double.class.getClassLoader());
        this.fillColor = in.readInt();
        this.strokeColor = in.readInt();
        this.strokeWidth = in.readInt();
        this.enableSatelliteView = in.readByte() != 0;
        int tmpDrawingType = in.readInt();
        this.drawingType = tmpDrawingType == -1 ? null : DrawingType.values()[tmpDrawingType];
    }

    public static final Parcelable.Creator<DrawingOption> CREATOR = new Parcelable.Creator<DrawingOption>() {
        @Override
        public DrawingOption createFromParcel(Parcel source) {
            return new DrawingOption(source);
        }

        @Override
        public DrawingOption[] newArray(int size) {
            return new DrawingOption[size];
        }
    };
}
