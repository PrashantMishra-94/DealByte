package com.deal.bytee.Utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.android.volley.RequestQueue;
import com.deal.bytee.Network.MyVolley;

public class App extends Application {

    public static App mInstance;
    public static RequestQueue mRequestQue;
    public static MySharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //FastSave.init(getApplicationContext());

        //registerReceiver(new ConnectivityChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        sharedPreferences = MySharedPreferences.getInstance(this);
        mRequestQue = MyVolley.getInstance().getRequestQueue();
        AppSignatureHelper appSignature = new AppSignatureHelper(this);
        appSignature.getAppSignatures();
    }
    public static App getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

}