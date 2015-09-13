package com.bawanj.criminalIntent;

import android.app.Application;
import android.content.Context;

/**
 * Created by jeffreychou on 9/12/15.
 */
public class CrimeApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate(){
        super.onCreate();
        sContext= getApplicationContext();
    }

    public static Context getGlobalContext(){
        return sContext;
    }
}
