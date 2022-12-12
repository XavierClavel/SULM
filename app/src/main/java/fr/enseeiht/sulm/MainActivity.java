package fr.enseeiht.sulm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static MainActivity instance;
    public static FusedLocationProviderClient fusedLocationClient;
    static boolean active = false;
    ImageButton image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


        instance = this;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        image = findViewById(R.id.imageButton);
        image.setOnClickListener(this);
        if (active) image.setImageResource(R.drawable.light_on);
    }

    public void onClick(View view) {
        active = !active;
        if (active) {
            startClientDetection();
            image.setImageResource(R.drawable.light_on);
        }
        else {
            stopClientDetection();
            image.setImageResource(R.drawable.light_off);
        }
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