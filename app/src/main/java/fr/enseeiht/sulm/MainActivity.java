package fr.enseeiht.sulm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static MainActivity instance;
    public static FusedLocationProviderClient fusedLocationClient;
    boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        findViewById(R.id.Switch).setOnClickListener(this);
    }

    public void onClick(View view) {
        active = !active;
        if (active) startClientDetection();
        else stopClientDetection();
    }

    void startClientDetection()
    {
        ContextCompat.startForegroundService(this, new Intent(this, ForegroundService.class));
    }

    void stopClientDetection()
    {
        ForegroundService.stopService();
    }


}