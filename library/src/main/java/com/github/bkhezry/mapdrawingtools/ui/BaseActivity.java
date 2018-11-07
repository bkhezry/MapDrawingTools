package com.github.bkhezry.mapdrawingtools.ui;


import android.Manifest;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import androidx.appcompat.app.AppCompatActivity;
import rx.functions.Action1;

public abstract class BaseActivity extends AppCompatActivity {

  @Override
  protected void onStart() {
    super.onStart();
    RxPermissions rxPermissions = new RxPermissions(this);
    rxPermissions
        .request(Manifest.permission.ACCESS_FINE_LOCATION)
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean granted) {
            if (granted) {
              onLocationPermissionGranted();
            } else {
              Toast.makeText(BaseActivity.this, "Sorry, App did not work without Location permission", Toast.LENGTH_SHORT).show();
            }
          }
        });
  }

  protected abstract void onLocationPermissionGranted();
}
