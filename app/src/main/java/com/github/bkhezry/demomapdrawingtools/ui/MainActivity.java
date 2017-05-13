package com.github.bkhezry.demomapdrawingtools.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.bkhezry.mapdrawingtools.model.DrawingOption;
import com.github.bkhezry.mapdrawingtools.model.DrawingOptionBuilder;
import com.github.bkhezry.demomapdrawingtools.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppCompatButton mapButton = (AppCompatButton) findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new DrawingOptionBuilder()
                                .withLocation(35.659920, 51.621036)
                                .withFillColor(Color.argb(60, 0, 0, 255))
                                .withStrokeColor(Color.argb(100, 0, 0, 0))
                                .withStrokeWidth(3)
                                .withRequestGPSEnabling(false)
                                .withDrawingType(DrawingOption.DrawingType.POLYLINE)
                                .build(getApplicationContext());
                startActivity(intent);
            }
        });
    }

}
