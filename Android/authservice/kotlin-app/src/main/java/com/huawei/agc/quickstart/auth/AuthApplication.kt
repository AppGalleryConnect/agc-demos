package com.huawei.agc.quickstart.auth

import android.app.Application
import android.content.Context
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hms.api.HuaweiMobileServicesUtil

class AuthApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // for hms
        HuaweiMobileServicesUtil.setApplication(this)
    }

    /**
     * set default route policy
     * suggest to call in application#oncreate()
     *
     * @param context applicationContext
     */
    private fun setDefaultStorageLocation(context: Context) {
        val builder = AGConnectOptionsBuilder()
                .setRoutePolicy(AGCRoutePolicy.SINGAPORE)
        AGConnectInstance.initialize(context, builder)
        //now the location of auth is  AGCRoutePolicy.SINGAPORE
        val auth = AGConnectAuth.getInstance()
    }

    /**
     * set route policy
     *
     * @param context applicationContext
     */
    private fun setStorageLocation(context: Context) {
        val builder = AGConnectOptionsBuilder()
                .setRoutePolicy(AGCRoutePolicy.SINGAPORE)
        AGConnectInstance.initialize(context, builder)

        //the route policy of authSingapore is AGCRoutePolicy.SINGAPORE
        val authSingapore = AGConnectAuth.getInstance()
        val agConnectOptions = AGConnectOptionsBuilder()
                .setRoutePolicy(AGCRoutePolicy.GERMANY)
                .build(context)
        val agConnectInstance = AGConnectInstance.buildInstance(agConnectOptions)

        //the route policy of authGermany is AGCRoutePolicy.GERMANY
        val authGermany = AGConnectAuth.getInstance(agConnectInstance)
    }
}