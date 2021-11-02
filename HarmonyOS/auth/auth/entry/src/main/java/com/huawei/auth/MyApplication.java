package com.huawei.auth;

import com.huawei.agconnect.AGConnectInstance;
import ohos.aafwk.ability.AbilityPackage;

public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        super.onInitialize();
        AGConnectInstance.initialize(MyApplication.this);
    }
}
