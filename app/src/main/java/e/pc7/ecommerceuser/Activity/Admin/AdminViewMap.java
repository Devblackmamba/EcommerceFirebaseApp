package e.pc7.ecommerceuser.Activity.Admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rodolfonavalon.shaperipplelibrary.ShapeRipple;

import java.util.List;
import java.util.Locale;

import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.utils.Consts;

public class AdminViewMap extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView personaddress,map_customername;
    GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000;
    boolean hasLocation = true;
    double lat, lng;
    String locality,fulladdress,customername;
    ShapeRipple customer_map_pulse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_map);
        getSupportActionBar().hide();

        getSupportActionBar().hide();

        bindview();
        buildGoogleApiClient();
        initilizeMap();
        personaddress=findViewById(R.id.personaddress);

        lat=Double.valueOf(getIntent().getStringExtra("LAT"));
        lng=Double.valueOf(getIntent().getStringExtra("LNG"));
        fulladdress=getIntent().getStringExtra("FULL_ADDRESS");
        customername=getIntent().getStringExtra("CUSTOMER_NAME");

        map_customername.setText(customername);
    }

    private void initilizeMap() {
        if (googleMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.customerlocation_map)).getMapAsync(this);

            String setcolor = "#9fbfdf";
            customer_map_pulse.setEnableSingleRipple(true);
            customer_map_pulse.setRippleColor(Color.parseColor(setcolor));
            customer_map_pulse.startRipple();

            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        if (hasLocation) {
            LatLng currentPosition = new LatLng(lat, lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));
        }
        else {
            if (Consts.isLocationPermissionAvailable(AdminViewMap.this)) {
                connectToGoogleClient();
            }
            this.googleMap.setMyLocationEnabled(true);
        }

        this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //get latlng at the center by calling
                LatLng midLatLng = googleMap.getCameraPosition().target;
                lat = midLatLng.latitude;
                lng = midLatLng.longitude;
                personaddress.setText(fulladdress);
            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                int index = returnedAddress.getMaxAddressLineIndex();
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= index; i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                locality = returnedAddress.getLocality();

                Log.w("Location", strReturnedAddress.toString() + "----" + returnedAddress.getLocality());
            } else {
                Log.w("Location", "No AddressModel returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Location", "Cannot get AddressModel!");
        }
        return strAdd;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Consts.LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    connectToGoogleClient();
                } else {
                }
        }
    }

    private void connectToGoogleClient() {
        if (!Consts.isGpsEnabled(AdminViewMap.this)) {
            Consts.requestForDirectGps(AdminViewMap.this);
        } else {
            if (!hasLocation) {
                if (googleApiClient != null) {
                    googleApiClient.connect();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Consts.LOCATION_SETTINGS_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        connectToGoogleClient();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkPlayServices()) {
            Toast.makeText(AdminViewMap.this, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private void startLocationUpdates() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(AdminViewMap.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AdminViewMap.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(AdminViewMap.this, "You need to enable permissions to display location!", Toast.LENGTH_SHORT).show();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(AdminViewMap.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(AdminViewMap.this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {

            }
            return false;
        }
        return true;
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(AdminViewMap.this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();
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

    @Override
    public void onLocationChanged(Location location) {
    }

    private void bindview() {

        customer_map_pulse=findViewById(R.id.customer_map_pulse);
        personaddress=findViewById(R.id.personaddress);
        map_customername=findViewById(R.id.map_customername);
    }

}
