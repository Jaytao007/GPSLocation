package com.pec.gpslocation;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.pec.gpslocation.utils.LocationHelper;
import com.pec.gpslocation.utils.LocationUtils;

public class GPSService extends Service {

    public GPSService() {
    }

    public class GPSBinder extends Binder {
        public GPSService getService() {
            return GPSService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new GPSBinder();
    }

    public void startLocation(LocationHelper callBack) {
        LocationUtils.getInstance(this).initLocation(callBack);
    }

}
