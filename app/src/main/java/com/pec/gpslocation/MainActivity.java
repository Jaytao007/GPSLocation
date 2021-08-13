package com.pec.gpslocation;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pec.gpslocation.utils.EasyPermissionsEx;
import com.pec.gpslocation.utils.LocationHelper;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_CODE = 0x01;
    private String[] mNeedPermissionsList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private TextView textView;
    private GPSService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.location);
        checkPermissions();

    }


    private void checkPermissions() {
        if (EasyPermissionsEx.hasPermissions(MainActivity.this, mNeedPermissionsList)) {
            Intent intent = new Intent(this, GPSService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            EasyPermissionsEx.requestPermissions(MainActivity.this,
                    "需要定位权限来获取当地天气信息",
                    PERMISSION_REQ_CODE, mNeedPermissionsList);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("feeling", "已获取权限!");
                    Intent intent = new Intent(this, GPSService.class);
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                } else {
                    if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, mNeedPermissionsList)) {
                        EasyPermissionsEx.goSettings2Permissions(this, "需要定位权限来获取当地天气信息,但是该权限被禁止,你可以到设置中更改"
                                , "去设置", 1);
                    }
                }
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
        }
    }

    public void onclick(View v) {
        if (mService != null) {
            mService.startLocation(callBack);
        } else {
            Intent intent = new Intent(this, GPSService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GPSService.GPSBinder binder = (GPSService.GPSBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private LocationHelper callBack = new LocationHelper() {
        @Override
        public void UpdateLocation(Location location) {
            textView.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {

        }

        @Override
        public void UpdateGPSStatus(GpsStatus pGpsStatus) {

        }

        @Override
        public void UpdateLastLocation(Location location) {
            textView.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
