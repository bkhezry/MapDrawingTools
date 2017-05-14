package com.github.bkhezry.mapdrawingtools.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.bkhezry.mapdrawingtools.R;
import com.github.bkhezry.mapdrawingtools.model.DataModel;
import com.github.bkhezry.mapdrawingtools.model.DrawingOption;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public final static String MAP_OPTION = "map_option";
    public final static int REQUEST_CHECK_SETTINGS = 0;
    public static final String POINTS = "points";
    private GoogleMap mMap;
    private Location currentLocation;
    private List<LatLng> points = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private Polygon polygon;
    private Polyline polyline;
    private ReactiveLocationProvider locationProvider;
    private Observable<Location> lastKnownLocationObservable;
    private Observable<Location> locationUpdatesObservable;
    private Subscription lastKnownLocationSubscription;
    private Subscription updatableLocationSubscription;
    private Marker currentMarker;
    private CompositeSubscription compositeSubscription;
    private final static String TAG = "MapsActivity";
    private boolean isGPSOn = false;
    private GoogleApiClient mGoogleApiClient;
    private DrawingOption drawingOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        drawingOption = getIntent().getParcelableExtra(MAP_OPTION);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpFABs();
        initRequestingLocation();
        if (drawingOption.getRequestGPSEnabling())
            requestActivatingGPS();

    }

    private void setUpFABs() {
        final FloatingActionButton btnSatellite = (FloatingActionButton) findViewById(R.id.btnSatellite);
        btnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ? GoogleMap.MAP_TYPE_NORMAL : GoogleMap.MAP_TYPE_SATELLITE);
                btnSatellite.setImageResource(mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ? R.drawable.ic_satellite_off : R.drawable.ic_satellite_on);
            }
        });

        btnSatellite.setVisibility(drawingOption.getEnableSatelliteView() ? View.VISIBLE : View.GONE);
        FloatingActionButton btnUndo = (FloatingActionButton) findViewById(R.id.btnUndo);
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points.size() > 0) {
                    Marker marker = markerList.get(markerList.size() - 1);
                    marker.remove();
                    markerList.remove(marker);
                    points.remove(points.size() - 1);
                    if (points.size() > 0) {
                        if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYGON) {
                            drawPolygon(points);
                        } else if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYLINE) {
                            drawPolyline(points);
                        }
                    }
                }
            }
        });
        FloatingActionButton btnDone = (FloatingActionButton) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnCurrentPosition();
            }
        });
        FloatingActionButton btnGPS = (FloatingActionButton) findViewById(R.id.btnGPS);
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGPSOn) {
                    requestActivatingGPS();
                } else {
                    if (compositeSubscription != null && locationUpdatesObservable != null) {
                        getLastKnowLocation();
                    }
                }
            }
        });
    }


    private void initRequestingLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        compositeSubscription = new CompositeSubscription();
        locationProvider = new ReactiveLocationProvider(getApplicationContext());
        lastKnownLocationObservable = locationProvider.getLastKnownLocation();
    }


    private void requestActivatingGPS() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100);
        locationUpdatesObservable = locationProvider.getUpdatedLocation(locationRequest);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getLastKnowLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e(TAG, "Error happen during show Dialog for Turn of GPS");
                        break;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng center = new LatLng(drawingOption.getLocationLatitude(), drawingOption.getLocationLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, drawingOption.getZoom()));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                @IdRes int icon = R.drawable.ic_add_location_light_green_500_36dp;
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(MapsActivity.this, icon));
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(bitmap).draggable(true));
                marker.setTag(latLng);
                markerList.add(marker);
                points.add(latLng);
                if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYGON) {
                    drawPolygon(points);
                } else if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYLINE) {
                    drawPolyline(points);
                }


            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                updateMarkerLocation(marker);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                updateMarkerLocation(marker);
            }
        });
    }

    private void updateMarkerLocation(Marker marker) {
        LatLng latLng = (LatLng) marker.getTag();
        int position = points.indexOf(latLng);
        points.set(position, marker.getPosition());
        marker.setTag(marker.getPosition());
        if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYGON) {
            drawPolygon(points);
        } else if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYLINE) {
            drawPolyline(points);
        } else {

        }
    }

    private void drawPolyline(List<LatLng> latLngList) {
        if (polyline != null) {
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(drawingOption.getStrokeColor());
        polylineOptions.width(drawingOption.getStrokeWidth());
        polylineOptions.addAll(latLngList);
        polyline = mMap.addPolyline(polylineOptions);
    }


    private void drawPolygon(List<LatLng> latLngList) {
        if (polygon != null) {
            polygon.remove();
        }
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(drawingOption.getFillColor());
        polygonOptions.strokeColor(drawingOption.getStrokeColor());
        polygonOptions.strokeWidth(drawingOption.getStrokeWidth());
        polygonOptions.addAll(latLngList);
        polygon = mMap.addPolygon(polygonOptions);
    }

    @Override
    protected void onLocationPermissionGranted() {
        getLastKnowLocation();
        updateLocation();
    }

    private void updateLocation() {
        if (locationUpdatesObservable != null && compositeSubscription != null) {
            updatableLocationSubscription = locationUpdatesObservable
                    .subscribe(new Action1<Location>() {
                        @Override
                        public void call(Location location) {
                            if (currentLocation == null)
                                moveMapToCenter(location);

                            currentLocation = location;
                            moveMarkerCurrentPosition(location);
                        }
                    }, new ErrorHandler());
            compositeSubscription.add(updatableLocationSubscription);
        }
    }

    private void getLastKnowLocation() {
        if (lastKnownLocationObservable != null && compositeSubscription != null) {
            lastKnownLocationSubscription =
                    lastKnownLocationObservable
                            .subscribe(new Action1<Location>() {
                                @Override
                                public void call(Location location) {
                                    currentLocation = location;
                                    moveMapToCenter(location);
                                }
                            }, new ErrorHandler());
            compositeSubscription.add(lastKnownLocationSubscription);
        }
    }


    private class ErrorHandler implements Action1<Throwable> {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(MapsActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "Error occurred", throwable);
        }
    }

    public void moveMapToCenter(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            myLocationMarker(latLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    public void moveMarkerCurrentPosition(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            myLocationMarker(latLng);
        }
    }

    private void myLocationMarker(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.setPosition(latLng);
        } else {
            @IdRes int icon = R.drawable.ic_navigation_red_a400_36dp;
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(MapsActivity.this, icon));
            currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(bitmap)
                    .draggable(false));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (lastKnownLocationSubscription != null && updatableLocationSubscription != null && compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription.clear();
            updatableLocationSubscription.unsubscribe();
            lastKnownLocationSubscription.unsubscribe();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        // All required changes were successfully made
                        Log.d(TAG, "User enabled location");
                        getLastKnowLocation();
                        updateLocation();
                        isGPSOn = true;
                        break;
                    case RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d(TAG, "User Cancelled enabling location");
                        isGPSOn = false;
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void returnCurrentPosition() {
        if (points.size() > 0) {
            Intent returnIntent = new Intent();
            LatLng[] latLngs = new LatLng[points.size()];
            points.toArray(latLngs);
            DataModel dataModel = new DataModel();
            dataModel.setCount(points.size());
            dataModel.setPoints(latLngs);
            returnIntent.putExtra(POINTS, dataModel);
            setResult(RESULT_OK, returnIntent);

        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private static Bitmap getBitmapFromDrawable(Context context, int icon) {
        Drawable drawable = ContextCompat.getDrawable(context, icon);
        Bitmap obm = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(obm);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return obm;
    }
}
