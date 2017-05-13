package com.github.bkhezry.demomapdrawingtools.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.bkhezry.mapdrawingtools.utils.MapOptionBuilder;
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
                        new MapOptionBuilder()
                                .withLocation(35.659920, 51.621036)
                                .withDrawingType(MapOptionBuilder.DrawingType.POLYGON)
                                .build(getApplicationContext());
                startActivity(intent);
            }
        });
    }

}
