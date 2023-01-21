package com.deal.bytee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chaos.view.PinView;
import com.deal.bytee.R;
import com.deal.bytee.Utils.App;
import com.deal.bytee.Utils.Endpoints;
import com.deal.bytee.Utils.HexagonMaskView;
import com.deal.bytee.Utils.MySharedPreferences;
import com.deal.bytee.Utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    private LinearLayoutCompat llLogin;
    private AppCompatEditText edtLMobile;
    private AppCompatEditText edtLPass;
    private AppCompatButton btnLLogin;
    private AppCompatTextView tvLForgetPass;
    private AppCompatTextView tvLRegister;
    private AppCompatTextView tvLFacebook;
    private AppCompatImageView ivLTwit;
    private AppCompatTextView tvLGmail;
    private LinearLayoutCompat llRegister;
    private HexagonMaskView ivRUser;
    private AppCompatEditText edtRName;
    private AppCompatEditText edtREmail;
    private AppCompatEditText edtRMobile;
    private AppCompatEditText edtRPass;
    private AppCompatButton btnRRegister;
    Animation animShow, animHide;
    private AppCompatTextView tvRLogin;
    private FrameLayout llForgotPass;
    private LinearLayoutCompat llFBack;
    private AppCompatEditText edtFEmail;
    private AppCompatButton btnFSubmit;
    private FrameLayout llVerifyOTP;
    private LinearLayoutCompat llVOBack;
    private PinView edtOTP;
    private AppCompatButton btnVOTP;
    private boolean isFromDetails = false;
    private String businessId = null;

    public static String IS_FROM_DETAILS = "IS_FROM_DETAILS";
    public static String BUSINESS_ID = "BUSINESS_ID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.main));
        initView();
        isFromDetails = getIntent().getBooleanExtra(IS_FROM_DETAILS, false);
        businessId = getIntent().getStringExtra(BUSINESS_ID);
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
        llLogin.setVisibility(View.VISIBLE);
        llRegister.setVisibility(View.GONE);
        llForgotPass.setVisibility(View.GONE);
        llVerifyOTP.setVisibility(View.GONE);
        bindClick();
    }

    private void bindClick() {

        tvLRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llRegister.setVisibility(View.VISIBLE);
                llRegister.startAnimation(animShow);
                llLogin.startAnimation(animHide);
                llLogin.setVisibility(View.GONE);
            }
        });

        tvRLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llRegister.setVisibility(View.GONE);
                llRegister.startAnimation(animHide);
                llLogin.setVisibility(View.VISIBLE);
                llLogin.startAnimation(animShow);
            }
        });


        btnLLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // llVerifyOTP.setVisibility(View.VISIBLE);
               // llVerifyOTP.startAnimation(animShow);
               // llLogin.startAnimation(animHide);
               // llLogin.setVisibility(View.GONE);
               // startActivity(new Intent(LoginActivity.this,DashBoardActivity.class));

                checkForValidation();
            }
        });

        tvLForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llForgotPass.setVisibility(View.VISIBLE);
                llForgotPass.startAnimation(animShow);
                llLogin.startAnimation(animHide);
                llLogin.setVisibility(View.GONE);
            }
        });


        llFBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llForgotPass.setVisibility(View.GONE);
                llForgotPass.startAnimation(animHide);
                llLogin.setVisibility(View.VISIBLE);
                llLogin.startAnimation(animShow);
            }
        });

        llVOBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llVerifyOTP.setVisibility(View.GONE);
                llVerifyOTP.startAnimation(animHide);
                llLogin.setVisibility(View.VISIBLE);
                llLogin.startAnimation(animShow);
            }
        });


        btnVOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,DashBoardActivity.class));
            }
        });

        btnRRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateSignUp();
            }
        });

        btnFSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateReset();
            }
        });
    }

    private void ValidateReset() {
        String mobile = edtFEmail.getText().toString().trim();

        if (!MyUtils.isValidMobile(mobile)) {
            edtFEmail.requestFocus();
            edtFEmail.setError("Please enter valid mobile number!");
        } else {
            if (MyUtils.isNetworkAvailable()) {
                MyUtils.hideKeyboard(LoginActivity.this);
                MyUtils.showProgressDialog(this, false);
                apicallreset(mobile);
            } else {
                MyUtils.showTheToastMessage("Please Check Internet Connection!");
            }
        }

    }

    private void apicallreset(String mobile) {

        JSONObject js = new JSONObject();
        try {
            js.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.FORGET_PASS_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("loginMethod", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                llForgotPass.setVisibility(View.GONE);
                                llForgotPass.startAnimation(animHide);
                                llLogin.setVisibility(View.VISIBLE);
                                llLogin.startAnimation(animShow);
                                edtFEmail.setText("");
                            } else {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                llForgotPass.setVisibility(View.GONE);
                                llForgotPass.startAnimation(animHide);
                                llLogin.setVisibility(View.VISIBLE);
                                llLogin.startAnimation(animShow);
                                edtFEmail.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.dismisProgressDialog();
                String error_type = MyUtils.simpleVolleyRequestError("TAG", error);
                MyUtils.showTheToastMessage(error_type);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.mRequestQue.add(request);
    }


    private void ValidateSignUp() {

        String name = edtRName.getText().toString().trim();
        String email = edtREmail.getText().toString().trim();
        String mobile = edtRMobile.getText().toString().trim();
        String pass = edtRPass.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtRName.requestFocus();
            edtRName.setError("Please enter name!");
        } else if (!MyUtils.isValidEmail(email)) {
            edtREmail.requestFocus();
            edtREmail.setError("Please enter valid email!");
        } else if (!MyUtils.isValidMobile(mobile)) {
            edtRMobile.requestFocus();
            edtRMobile.setError("Please enter valid mobile number!");
        } else if (TextUtils.isEmpty(pass)) {
            edtRPass.requestFocus();
            edtRPass.setError("Please enter pass!");
        } /*else if (TextUtils.isEmpty(cpass)) {
            edtSignUpCPsw.requestFocus();
            edtSignUpCPsw.setError("Please enter pass!");
        } else if (!pass.equals(cpass)) {
            MyUtils.showTheToastMessage("Password Don't Match");
        }*/ else {
            if (MyUtils.isNetworkAvailable()) {
                MyUtils.hideKeyboard(LoginActivity.this);
                MyUtils.showProgressDialog(this, false);
                apicallregister(name, email, mobile, pass);
            } else {
                MyUtils.showTheToastMessage("Please Check Internet Connection!");
            }
        }

    }


    private void apicallregister(String name, String email, String mobile, String pass) {
        JSONObject js = new JSONObject();
        try {
            js.put("name", name);
            js.put("mobile", mobile);
            js.put("email", email);
            js.put("password", pass);
            js.put("address", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.REGISTER_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginMethod", response.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (response.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(response.getString("message"));
                                llRegister.setVisibility(View.GONE);
                                llRegister.startAnimation(animHide);
                                llLogin.setVisibility(View.VISIBLE);
                                llLogin.startAnimation(animShow);
                                edtRName.setText("");
                                edtREmail.setText("");
                                edtRMobile.setText("");
                                edtRPass.setText("");
                               // otp = String.valueOf(response.getInt("otp"));
                                //userid = String.valueOf(response.getString("userid"));
                               // mobilee = String.valueOf(response.getString("mobile"));
                              //  Log.d("TAG", "onResponse: OTP>>>" + otp);
                            } else {
                                MyUtils.showTheToastMessage(response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.dismisProgressDialog();
                String error_type = MyUtils.simpleVolleyRequestError("TAG", error);
                MyUtils.showTheToastMessage(error_type);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.mRequestQue.add(request);
    }



    private void checkForValidation() {

        String mobile = edtLMobile.getText().toString().trim();
        String pass = edtLPass.getText().toString().trim();

        if (!MyUtils.isValidMobile(mobile)){
            edtLMobile.requestFocus();
            edtLMobile.setError("Please enter valid mobile number");
        }else if (TextUtils.isEmpty(pass)){
            edtLPass.requestFocus();
            edtLPass.setError("Please enter valid password");
        }else {
            if (MyUtils.isNetworkAvailable()) {
                MyUtils.hideKeyboard(LoginActivity.this);
                MyUtils.showProgressDialog(this, false);
                apicalllogin(mobile, pass);
            } else {
                MyUtils.showTheToastMessage("Please Check Internet Connection!");
            }
        }
    }

    private void apicalllogin(String mobile, String pass) {
        JSONObject js = new JSONObject();
        try {
            js.put("mobile", mobile);
            js.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.LOGIN_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("loginMethod", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                App.sharedPreferences.setKey(MySharedPreferences.isLogin, MySharedPreferences.YES);
                                App.sharedPreferences.setKey(MySharedPreferences.id, obj.getString("userid"));
                                App.sharedPreferences.setKey(MySharedPreferences.name, obj.getString("name"));
                                App.sharedPreferences.setKey(MySharedPreferences.email, obj.getString("email"));
                                App.sharedPreferences.setKey(MySharedPreferences.mobile, obj.getString("mobile"));

                                Intent mainIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                if (isFromDetails) {
                                    mainIntent.putExtra(IS_FROM_DETAILS, true);
                                    mainIntent.putExtra(BUSINESS_ID, businessId);
                                }
                                LoginActivity.this.startActivity(mainIntent);
                                LoginActivity.this.finish();
                                finishAffinity();


                            } /*else if (obj.getInt("status") == -1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                lytLogin.setVisibility(View.GONE);
                                lytLogin.startAnimation(animHide);
                                lytOTP.setVisibility(View.VISIBLE);
                                lytOTP.startAnimation(animShow);
                                otp = String.valueOf(obj.getInt("otp"));
                                userid = String.valueOf(obj.getString("userid"));
                                mobilee = String.valueOf(obj.getString("mobile"));
                                Log.d("TAG", "onResponse: OTP>>>" + otp);
                            }*/ else {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.dismisProgressDialog();
                String error_type = MyUtils.simpleVolleyRequestError("TAG", error);
                MyUtils.showTheToastMessage(error_type);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.mRequestQue.add(request);

    }

    private void initView() {
        llLogin = (LinearLayoutCompat) findViewById(R.id.llLogin);
        edtLMobile = (AppCompatEditText) findViewById(R.id.edtLMobile);
        edtLPass = (AppCompatEditText) findViewById(R.id.edtLPass);
        btnLLogin = (AppCompatButton) findViewById(R.id.btnLLogin);
        tvLForgetPass = (AppCompatTextView) findViewById(R.id.tvLForgetPass);
        tvLRegister = (AppCompatTextView) findViewById(R.id.tvLRegister);
        tvLFacebook = (AppCompatTextView) findViewById(R.id.tvLFacebook);
        ivLTwit = (AppCompatImageView) findViewById(R.id.ivLTwit);
        tvLGmail = (AppCompatTextView) findViewById(R.id.tvLGmail);
        llRegister = (LinearLayoutCompat) findViewById(R.id.llRegister);
        ivRUser = (HexagonMaskView) findViewById(R.id.ivRUser);
        edtRName = (AppCompatEditText) findViewById(R.id.edtRName);
        edtREmail = (AppCompatEditText) findViewById(R.id.edtREmail);
        edtRMobile = (AppCompatEditText) findViewById(R.id.edtRMobile);
        edtRPass = (AppCompatEditText) findViewById(R.id.edtRPass);
        btnRRegister = (AppCompatButton) findViewById(R.id.btnRRegister);
        tvRLogin = (AppCompatTextView) findViewById(R.id.tvRLogin);
        llForgotPass = (FrameLayout) findViewById(R.id.llForgotPass);
        llFBack = (LinearLayoutCompat) findViewById(R.id.llFBack);
        edtFEmail = (AppCompatEditText) findViewById(R.id.edtFEmail);
        btnFSubmit = (AppCompatButton) findViewById(R.id.btnFSubmit);
        llVerifyOTP = (FrameLayout) findViewById(R.id.llVerifyOTP);
        llVOBack = (LinearLayoutCompat) findViewById(R.id.llVOBack);
        edtOTP = (PinView) findViewById(R.id.edtOTP);
        btnVOTP = (AppCompatButton) findViewById(R.id.btnVOTP);
    }
}