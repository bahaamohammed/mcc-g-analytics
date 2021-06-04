package com.bahaadev.mcc_g_analytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AppAnalytics {
    private final FirebaseAnalytics mFirebaseAnalystic;
    String android_id;

    @SuppressLint("HardwareIds")
    public AppAnalytics(Context context) {
        mFirebaseAnalystic = FirebaseAnalytics.getInstance(context);
         android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    public void trackScreen(String screenName, String screenClass) {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS,screenClass);
        mFirebaseAnalystic.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,bundle);
    }

    public void UserSpends(String pageName,String className,String timeCount){
        Bundle bundle = new Bundle();
        bundle.putString("deviceID",this.android_id);
        bundle.putString("pageName",pageName);
        bundle.putString("className",className);
        bundle.putString("timeCount",timeCount);
        mFirebaseAnalystic.logEvent("user_spend_seconds",bundle);
    }

}
