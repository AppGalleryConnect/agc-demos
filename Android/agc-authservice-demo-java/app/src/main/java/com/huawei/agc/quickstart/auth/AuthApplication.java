package com.huawei.agc.quickstart.auth;

import android.app.Application;

import com.huawei.hms.api.HuaweiMobileServicesUtil;

public class AuthApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HuaweiMobileServicesUtil.setApplication(this);
    }
}
