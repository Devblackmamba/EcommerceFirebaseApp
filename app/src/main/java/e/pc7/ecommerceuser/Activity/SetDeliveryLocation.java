package e.pc7.ecommerceuser.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rodolfonavalon.shaperipplelibrary.ShapeRipple;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import e.pc7.ecommerceuser.Prevenlent.Observer;
import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.utils.Consts;

public class SetDeliveryLocation extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,View.OnClickListener {

    GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000;
    boolean hasLocation = false;
    double lat, lng;
    String locality;

    String autolocation,area,houseorflat,landmark;
    ProgressDialog progressDialog;

    AppBarLayout map_bar;
    EditText tv_location, tv_area, tv_house_flat, tv_landmark;
    Button btn_save_address;
    ShapeRipple ripple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_delivery_location);
        getSupportActionBar().hide();

        bindview();
        stopappbarscroll();
        buildGoogleApiClient();
        initilizeMap();

        //onclickListning
        btn_save_address.setOnClickListener(this);
        progressDialog= new ProgressDialog(this);
    }

    private void initilizeMap() {
        if (googleMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.customer_map)).getMapAsync(this);

            String setcolor = "#9fbfdf";
            ripple.setEnableSingleRipple(true);
            ripple.setRippleColor(Color.parseColor(setcolor));
            ripple.startRipple();

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
        if (hasLocation) {
            LatLng currentPosition = new LatLng(lat, lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));
        }
        else {
            if (Consts.isLocationPermissionAvailable(SetDeliveryLocation.this)) {
                connectToGoogleClient();
            }
            this.googleMap.setMyLocationEnabled(true);
        }

        this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //get latlng at the center by calling
                LatLng midLatLng = googleMap.getCameraPosition().target;
                tv_location.setText(getCompleteAddressString(midLatLng.latitude, midLatLng.longitude));
                lat = midLatLng.latitude;
                lng = midLatLng.longitude;
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
                for (int i = 0; i<= index; i++) {
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
        if (!Consts.isGpsEnabled(SetDeliveryLocation.this)) {
            Consts.requestForDirectGps(SetDeliveryLocation.this);
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
            Toast.makeText(SetDeliveryLocation.this, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(SetDeliveryLocation.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SetDeliveryLocation.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(SetDeliveryLocation.this, "You need to enable permissions to display location!", Toast.LENGTH_SHORT).show();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(SetDeliveryLocation.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(SetDeliveryLocation.this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {

            }
            return false;
        }
        return true;
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(SetDeliveryLocation.this).
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
        map_bar = findViewById(R.id.map_bar);
        btn_save_address = findViewById(R.id.btn_save_address);
        tv_location = findViewById(R.id.tv_location);
        tv_area = findViewById(R.id.tv_area);
        tv_house_flat = findViewById(R.id.tv_house_flat);
        tv_landmark = findViewById(R.id.tv_landmark);
        ripple=findViewById(R.id.location_pulse);
    }



    //disable appbar to scroll
    private void stopappbarscroll() {
        if (map_bar.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) map_bar.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_save_address:
                 saveaddressdata();
                break;
        }

    }


    //STORE ADDRESS TO FIREBASE SERVER
    private void saveaddressdata(){

        CharSequence titile="progressing..",messgae="please wait";

        progressDialog.setTitle(titile);
        progressDialog.setMessage(messgae);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        autolocation=tv_location.getText().toString();
        area=tv_area.getText().toString();
        houseorflat=tv_house_flat.getText().toString();
        landmark=tv_landmark.getText().toString();

        Consts.addresstoprint=autolocation;

        DatabaseReference addressref = FirebaseDatabase.getInstance().getReference().child("Users").child(Observer.CurrentOnlineUser.appusername);

        HashMap<String,Object> addressmap = new HashMap<>();
        addressmap.put("userlocation",autolocation);
        addressmap.put("userarea",area);
        addressmap.put("userhouseorflat",houseorflat);
        addressmap.put("userlandmark",landmark);
        addressmap.put("lat",String.valueOf(lat));
        addressmap.put("lng",String.valueOf(lng));

        addressref.child("address").updateChildren(addressmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                     progressDialog.dismiss();

                     if(getIntent().hasExtra("EMPTY_ADDRESS"))
                     {
                         Consts.locationisavailable=1;
                         Intent intent = new Intent(SetDeliveryLocation.this,ConfirmFinalorderactivity.class);
                         startActivity(intent);
                         finish();

                     }
                     else{
                         Intent intent = new Intent(SetDeliveryLocation.this,ShoppingSreenActivity.class);
                         startActivity(intent);
                         finish();
                     }
                }
            }
        });
    }
}

