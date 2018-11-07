package com.github.bkhezry.mapdrawingtools.utils;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

public class CalUtils {
  public static double getArea(List<LatLng> latLngs) {
    return SphericalUtil.computeArea(latLngs);
  }

  public static double getLength(List<LatLng> latLngs) {
    return SphericalUtil.computeLength(latLngs);
  }
}
