package e.pc7.ecommerceuser.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.net.URLEncoder;

import static android.content.Context.LOCATION_SERVICE;

public class Consts {

    public static String addresstoprint="";

    public static final String RIDE = "ride";
    public static final String FOOD = "food";
    public static final String PARCEL = "parcel";
    public static final String OTHER = "other";


    public static  int locationisavailable=0;

    public static String latitude1 = "latitude1";
    public static String longitude1 = "longitude1";

    public static final String MALE="male";
    public static final String FEMALE="female";
    public static final String EMAIL="EMAIL";
    public static final String PHONE="EMAIL";
    public static final String TWITTER="TWITTER";
    public static final String FACEBOOK="FACEBOOK";
    public static final String GOOGLE="GOOGLE";

    public static final int TYPE_LOCATION=0;
    public static final int TYPE_ADDRESS=1;
    public static final int TYPE_SEARCHES=2;
    public static final int TYPE_HEADER=3;

    public static final String ADDRESS_HOME="home";
    public static final String ADDRESS_WORK="work";
    public static final String ADDRESS_OTHER="other";


    public static final String ADDRESS_LIST="list";
    public static final String ADDRESS_CART="cart";

    public static final int LOCATION_PERMISSION_REQUEST_CODE =1;
    public static final int LOCATION_SETTINGS_REQUEST =2;


    public static int networksignal;
    public static String printtime;
    public static int lastpos;


    public static void openWhatsappChat(Context context, String phoneNumber, String message) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    context.startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class Extras
    {
        public static final String EXTRA_LAT="EXTRA_LAT";
        public static final String EXTRA_LNG="EXTRA_LNG";
        public static final String EXTRA_PLACE_NAME="EXTRA_PLACE_NAME";
        public static final String EXTRA_PLACE_ADDRESS="EXTRA_PLACE_ADDRESS";
        public static final String EXTRA_EDIT="EXTRA_EDIT";
        public static final String EXTRA_ADD="EXTRA_ADD";
    }



    public static class Prefs {
        public static final String KEY_ISLOGIN = "KEY_ISLOGIN";
        public static final String KEY_LOGIN_TYPE = "KEY_LOGIN_TYPE";
        public static final String KEY_NAME = "KEY_NAME";
        public static final String KEY_EMAIL = "KEY_EMAIL";
        public static final String KEY_MOBILENO = "KEY_MOBILENO";
        public static final String KEY_COUNTRYCODE = "KEY_COUNTRYCODE";
        public static final String KEY_GENDER = "KEY_GENDER";
        public static final String KEY_BIRTHDATE ="KEY_BIRTHDATE";
        public static final String KEY_PROFILEURL ="KEY_PROFILEURL";

        public static final String CHECKOUTLIST ="CHECKOUTLIST";
        public static final String ITEMCOUNT ="ITEMCOUNT";
        public static final String CHECKOUTAMOUNT ="CHECKOUTAMOUNT";
        public static final String CURRENT_RESTUARANT ="CURRENT_RESTUARANT";
        public static final String PASTORDER_HISTORY ="PASTORDER_HISTORY";


        public static final String KEY_SELECTED_LOCATION="KEY_SELECTED_LOCATION";
        public static final String KEY_RECENT_SEARCH="KEY_RECENT_SEARCH";
        public static final String KEY_SAVED_ADDRESS="KEY_SAVED_ADDRESS";


    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }


    public static void requestForDirectGps(final Activity activity) {

        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setNeedBle(true);
        final Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response =
                            task.getResult(ApiException.class);
                } catch (ApiException ex) {
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException.startResolutionForResult(activity,LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });

    }

    public static boolean isLocationPermissionAvailable(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            return true;
        }
        return false;
    }

}
