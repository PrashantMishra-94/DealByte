package com.deal.bytee.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.deal.bytee.model.CartAddVoucherModel;

import java.util.ArrayList;

public class MySharedPreferences {
    //instance field
    private static SharedPreferences mSharedPreference;
    private static MySharedPreferences mInstance = null;
    private static Context mContext;

    // Jetli Jarur Hoi Etlu Add Karvanu
    public static final String id = "id";
    public static final String name = "name";
    public static final String cashback_earned = "cashback_earned";
    public static final String email = "email";
    public static final String mobile = "mobile";
    public static final String pass = "pass";
    public static final String address = "address";
    public static final String profile_pic = "profile_pic";
    public static final String aadhar_number = "aadhar_number";
    public static final String aadhar_front = "aadhar_front";
    public static final String aadhar_back = "aadhar_back";
    public static final String pan_number = "pan_number";
    public static final String pan_front = "pan_front";
    public static final String lat = "lat";
    public static final String longi = "long";
    public static final String kyc_verify = "kyc_verify";
    public static final String AuthToken = "AuthToken";
    public static String RAZOR_PAY_KEY_VALUE = "rzp_live_dAV0Qg5WOMrdIL";
    // boolean value Add
    public static final String YES = "yes";
    public static final String NO = "no";
    //Shared Preference key
    private String KEY_PREFERENCE_NAME = "dealbyte"; // put Application Name
    //private keyS
    public String KEY_DEFAULT = null;
    public static String isLogin = "isLoggedIn";
    public static String isFromDetails = "isFromDetails";
    public static String isSubscribe = "isSubscribe";
    public static String isFromBuy = "isFromBuy";

    SharedPreferences.Editor editor;

    public MySharedPreferences() {
        mSharedPreference = mContext.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreference.edit();
    }

    public static MySharedPreferences getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new MySharedPreferences();
        }
        return mInstance;
    }

    public static boolean getIsLogin() {
        return mSharedPreference.contains(isLogin) && mSharedPreference.getBoolean(isLogin, false);
    }
    public static void setIsLogin(boolean isLoginn) {
        mSharedPreference.edit().putBoolean(isLogin, isLoginn);
        mSharedPreference.edit().commit();
    }


    //Method to store user Mobile number
    public boolean setKey(String keyname, String mobile) {
        mSharedPreference.edit().putString(keyname, mobile).apply();
        return false;
    }

    //Method to get User mobile number
    public String getKey(String keyname) {
        return mSharedPreference.getString(keyname, KEY_DEFAULT);
    }

    public Boolean chk(String key) {
        return mSharedPreference.contains(key);
    }

    public static void ClearAllData(){
        mSharedPreference.edit().clear().apply();
    }

}
