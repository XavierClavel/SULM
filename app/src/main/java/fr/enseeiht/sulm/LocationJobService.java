package fr.enseeiht.sulm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

public class LocationJobService extends JobService {
    public static boolean shouldReschedule = true;
    LocationJob locationJob;
    public static LocationJobService instance;
    public static JobParameters locationJobParameters;

    // Méthode appelée quand la tâche est lancée
    @Override
    public boolean onStartJob(JobParameters params) {
        //shouldReschedule = true;
        Log.d("location job", "onStartJob id=" + params.getJobId());
        // ***** Lancer ici la mesure dans un thread à part *****
        instance = this;
        locationJobParameters = params;
        locationJob = locationJob == null ? new LocationJob() : locationJob;
        locationJob.doInBackground();
        return true;
    }

    // Méthode appelée quand la tâche est arrêtée par le scheduler
// Retourne vrai si le scheduler doit relancer la tâche
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("location job", "onStopJob id=" + params.getJobId());
// ***** Arrêter le thread du job ici ******

        return shouldReschedule;
    }


    class LocationJob extends AsyncTask<String, Integer, String> {
        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            Log.d("location job", "location job started");
            try {
                getLocationData();
            } catch (Exception e) {
                Log.d("wifi job", "failed to read location data");
            } finally {
                LocationJobService.instance.jobFinished(LocationJobService.locationJobParameters, LocationJobService.shouldReschedule);
                ForegroundService.scheduleJobs();
            }
            return "Done";
        }

        @SuppressLint("MissingPermission")
        void getLocationData() {
            checkPermission();

            MainActivity.fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, new CancellationToken() {
                @Override
                public boolean isCancellationRequested() {
                    return false;
                }

                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }
            }).addOnSuccessListener(MainActivity.instance, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Get current location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Log.d("location", "user location is : " + location.toString());
                        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        RequestInterface.sendRequest(currentPosition);
                        Log.d("location job", "__________________________________________________________");
                        Float accuracy = null;
                        if (location.hasAccuracy()) accuracy = location.getAccuracy();
                    }
                }
            });
            return;
        }
    }

    public static void checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(MainActivity.instance, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(MainActivity.instance, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission3 = ContextCompat.checkSelfPermission(MainActivity.instance, Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        // Check for permissions
        if (permission1 != PackageManager.PERMISSION_GRANTED ||
            permission2 != PackageManager.PERMISSION_GRANTED ||
            permission3 != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission", "Requesting Permissions");

            // Request permissions
            ActivityCompat.requestPermissions(MainActivity.instance,
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 565);
        }

        else Log.d("permission", "Permissions Already Granted");
    }
}
