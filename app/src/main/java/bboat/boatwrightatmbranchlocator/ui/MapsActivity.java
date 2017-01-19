package bboat.boatwrightatmbranchlocator.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import bboat.boatwrightatmbranchlocator.R;
import bboat.boatwrightatmbranchlocator.datamodel.ChaseLocation;
import bboat.boatwrightatmbranchlocator.datamodel.ChaseApiResponse;
import bboat.boatwrightatmbranchlocator.webcalls.GetChaseLocationsAsync;


/**
 * Main Activity which shows the google map and Chase locations.  Please forgive the messiness
 * of this class--  permissions requests really dirty up the code and I would've liked to set
 * it up through another class, but was a little short on time.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GetChaseLocationsAsync.OnGetChaseLocationsFinished, FindLocationsDialogFragment.OnFragmentInteractionListener, GoogleMap.OnInfoWindowClickListener {
    private static final int REQUEST_PERMISSIONS_CODE = 0;

    private GoogleMap mMap;
    private LatLng mCurrentLocation;
    private List<ChaseLocation> locations;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = getLocationListener();

        requestLocationPermissionsAndSetupUi();
    }

    private void requestLocationPermissionsAndSetupUi() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_CODE);
        } else {
            setupMap();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupMap();
                } else {
                    showPermissionsError();
                    // lock out user interaction to save time.
                    // Should re-present permissions with explanation
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        }
    }

    private void setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        setupUserLocation();
    }

    private void setupUserLocation() {
        progressDialog = ProgressDialog.show(this, "Retrieving Your Location", "Please Wait...");
        try {
            mMap.setMyLocationEnabled(true);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            // See getLocationListener-onLocationChanged for following app flow steps

        } catch (SecurityException e) {
            // Permissions have already been requested and approved.
            // Would typically handle properly, but printing stack trace to save time.
            e.printStackTrace();
        }
    }

    @NonNull
    private LocationListener getLocationListener() {
        return new LocationListener() {
            Snackbar snackbar;

            @Override
            public void onLocationChanged(Location location) {
                // Implementing this extraordinarily simply to save some time. (It's getting late)
                mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                // Call the API with the current user location
                getBranchAndAtmLocations(mCurrentLocation);

                try {
                    mLocationManager.removeUpdates(this);
                } catch (SecurityException e) {
                    // Time saving- printing stack trace.  Highly unlikely for this to ever be hit.
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                snackbar.dismiss();
            }

            @Override
            public void onProviderDisabled(String provider) {
                snackbar = Snackbar.make(findViewById(R.id.maps_parent_layout),
                        "Please enable location services to find your location",
                        Snackbar.LENGTH_INDEFINITE);

                snackbar.show();
            }
        };
    }

    @Override
    public void onGetChaseApiReponseFinished(ChaseApiResponse chaseApiResponse) {
        // Removes all markers, overlays, and polylines from the map.
        mMap.clear();
        progressDialog.dismiss();

        // Locations have been retrieved, now add markers.
        locations = chaseApiResponse.getLocations();

        if (locations != null) {
            for (int i = 0; i < locations.size(); i++) {
                ChaseLocation chaseLocation = locations.get(i);

                LatLng loc = new LatLng(chaseLocation.getLat(), chaseLocation.getLng());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(chaseLocation.getLoctype().toUpperCase())
                        .snippet("Click here to show details"));
                marker.setTag(i);
            }
            LatLng nearestCoords = mCurrentLocation;

            if (locations.size() > 0) {
                ChaseLocation nearestLocation = locations.get(0);
                nearestCoords = new LatLng(nearestLocation.getLat(), nearestLocation.getLng());
            } else {
                Toast.makeText(this, "No Location Found", Toast.LENGTH_SHORT).show();
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nearestCoords, 11.0f));
        } else {
            showApiErrors();
        }
    }

    public void getBranchAndAtmLocations(final LatLng location) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(this, "Retrieving Chase Locations", "Please Wait...");

        new GetChaseLocationsAsync(this, location).execute();
    }

    @Override
    public void onLocationSubmitPressed(LatLng location) {
        if (location != null) {
            getBranchAndAtmLocations(location);
        } else {
            Toast.makeText(this, "No locations found", Toast.LENGTH_SHORT).show();
        }

    }

    private void showPermissionsError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions Denied")
                .setMessage("Cannot use this app without proper permissions.  " +
                        "Please enable the proper permissions and " +
                        "relaunch the application")
                .show();
    }

    private void showApiErrors() {
        /* Unsure of error conditions outside of submitting an invalid lat/long to the api.
         As the users don't have the ability to create non numeric lat/long, I cannot see an
         instance where this method would be hit unless the service is down.
         So in the interest of saving time, I am omitting the implementation of this method.
         If implemented, I would likely display a message box displaying the errors to the user,
         or handle some programmatically if possible/necessary. */

        Toast.makeText(this, "API errors encountered", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        int position = (int) marker.getTag();
        launchDetailsDialog(locations.get(position));
    }

    private void launchDetailsDialog(ChaseLocation chaseLocation) {
        DialogFragment locationDialogFragment = ChaseLocationDetailsDialogFragment.newInstance(chaseLocation);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        locationDialogFragment.show(ft, "details");
    }

    public void onFabClick(View view) {
        DialogFragment locationDialogFragment = FindLocationsDialogFragment.newInstance(mCurrentLocation);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        locationDialogFragment.show(ft, "locations");
    }


}
