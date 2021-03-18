package com.huawei.agc.quickstart.auth;

import android.app.Application;
import android.content.Context;

import com.huawei.agconnect.AGCRoutePolicy;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.AGConnectOptions;
import com.huawei.agconnect.AGConnectOptionsBuilder;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.hms.api.HuaweiMobileServicesUtil;

public class AuthApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // for hms
        HuaweiMobileServicesUtil.setApplication(this);
    }

    /**
     * set default route policy
     * suggest to call in application#oncreate()
     *
     * @param context applicationContext
     */
    private void setDefaultStorageLocation(Context context) {
        AGConnectOptionsBuilder builder = new AGConnectOptionsBuilder()
            .setRoutePolicy(AGCRoutePolicy.SINGAPORE);
        AGConnectInstance.initialize(context, builder);
        //now the location of auth is  AGCRoutePolicy.SINGAPORE
        AGConnectAuth auth = AGConnectAuth.getInstance();
    }

    /**
     * set route policy
     *
     * @param context applicationContext
     */
    private void setStorageLocation(Context context) {
        AGConnectOptionsBuilder builder = new AGConnectOptionsBuilder()
            .setRoutePolicy(AGCRoutePolicy.SINGAPORE);
        AGConnectInstance.initialize(context, builder);

        //the route policy of authSingapore is AGCRoutePolicy.SINGAPORE
        AGConnectAuth authSingapore = AGConnectAuth.getInstance();

        AGConnectOptions agConnectOptions = new AGConnectOptionsBuilder()
            .setRoutePolicy(AGCRoutePolicy.GERMANY)
            .build(context);
        AGConnectInstance agConnectInstance = AGConnectInstance.buildInstance(agConnectOptions);

        //the route policy of authGermany is AGCRoutePolicy.GERMANY
        AGConnectAuth authGermany = AGConnectAuth.getInstance(agConnectInstance);
    }
}
