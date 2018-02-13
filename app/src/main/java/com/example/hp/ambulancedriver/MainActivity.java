package com.example.hp.ambulancedriver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

public class MainActivity extends AppCompatActivity {

    // Request CODE use as Constarint
    final int REQUEST_CODE=123;
   // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 1000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1;

    // Set LOCATION_PROVIDER here:
    String LOCATION_PROVIDER_GPS = LocationManager.GPS_PROVIDER;
 //   String LOCATION_PROVIDER_NTW = LocationManager.NETWORK_PROVIDER;

    // LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    String longitude;
    String latitude;



    String urlLink = "http://192.168.225.42:3000/users.json";
  //  String APP_ID = "c9e3f70e376c436f8acaf82a6a40f3fd025eff0b9f29346176c8baec3ea5432b08d9b63ab571be3030c951f805dfb6812450e4c4e19a71db711f39141eb13951";
    final RequestParams params = new RequestParams();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn = (Button) findViewById(R.id.btn);
        final EditText deviceId = (EditText) findViewById(R.id.DeviceID);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String btnText = (String) btn.getText();
                String id = deviceId.getText().toString();

                if(!(id.isEmpty()) && btnText.equals("ON") ) {

                    Log.d("Ambi_App", "ON presed");
                    deviceId.setEnabled(false);
                    btn.setText("OFF");
                    params.put("id",id);
                    Log.d("Ambi_App", "Getting Current Location in button click()");
                    getCurrentLocation();
                    //letsDoSomeNetworking(params);

                }
                if(btnText.equals("OFF")) {

                    Log.d("Ambi_App", "OFF presed");
                    deviceId.setEnabled(true);
                    deviceId.setText(null);
                    btn.setText("ON");
                    mLocationManager.removeUpdates(mLocationListener);

                }

            }
        });


    }
    protected void onResume() {
        super.onResume();
        //getCurrentLocation();
        Log.d("Ambi_App", "Starting on resume() Function ");
    }


    void getCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("Ambi_App", "onLocationChanged() callback Recevied");

                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());

                Log.d("Ambi_App","Longitude value = " + longitude);
                Log.d("Ambi_App","Latitude value = " + latitude);

               // params.put("app",APP_ID);
                params.put("lat",latitude);
                params.put("long",longitude);

                letsDoSomeNetworking(params);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Ambi_App", "onProviderEnabled() callback Received");

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Ambi_App", "onProviderDisable() callback Received");

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //        -: Consider calling :-
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER_GPS, MIN_TIME, MIN_DISTANCE, mLocationListener);
      //  mLocationManager.requestLocationUpdates(LOCATION_PROVIDER_NTW, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Ambi_App","onRequestPermissionResult() : Permission Granted! ");
                //getCurrentLocation();
            }
            else{

                Log.d("Ambi_App"," Permission Denied =( ");
            }
        }
    }

    private void letsDoSomeNetworking(RequestParams params){

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(urlLink, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statuscode, Header[] headers, JSONObject response){
                Log.d("OUR_App","Success! JSON: " + response.toString());
            }

            @Override
            public void onFailure(int statuscode, Header[] headers, Throwable e, JSONObject response){
                //Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
