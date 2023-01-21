package com.deal.bytee.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deal.bytee.R;
import com.deal.bytee.Utils.App;
import com.deal.bytee.Utils.Endpoints;
import com.deal.bytee.Utils.MySharedPreferences;
import com.deal.bytee.Utils.MyUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONException;
import org.json.JSONObject;


public class ChangePassFragment extends BaseFragment {

    View view;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private LinearLayoutCompat llTop;
    private AppCompatImageView ivBack;
    private AppCompatTextView tvSave;
    private LinearLayout linearAccount;
    private AppCompatEditText edtOldPass;
    private LinearLayout linearAccNum;
    private AppCompatEditText edtNewPass;
    private LinearLayout linearBankCode;
    private AppCompatEditText edtCOnfirmPass;
    private AppCompatButton btnUpdate;

    public ChangePassFragment() {
        // Required empty public constructor
        super(R.layout.fragment_change_pass);
    }

    private void initView(View view) {
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        llTop = (LinearLayoutCompat) view.findViewById(R.id.llTop);
        ivBack = (AppCompatImageView) view.findViewById(R.id.ivBack);
        tvSave = (AppCompatTextView) view.findViewById(R.id.tvSave);
        linearAccount = (LinearLayout) view.findViewById(R.id.linear_account);
        edtOldPass = (AppCompatEditText) view.findViewById(R.id.edtOldPass);
        linearAccNum = (LinearLayout) view.findViewById(R.id.linear_acc_num);
        edtNewPass = (AppCompatEditText) view.findViewById(R.id.edtNewPass);
        linearBankCode = (LinearLayout) view.findViewById(R.id.linear_bank_code);
        edtCOnfirmPass = (AppCompatEditText) view.findViewById(R.id.edtCOnfirmPass);
        btnUpdate = (AppCompatButton) view.findViewById(R.id.btnUpdate);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final ChangePassFragment instance() {
            return new ChangePassFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        initView(view);
        bindClick();
        return view;

    }

    private void bindClick() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePass();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackStack();
            }
        });
    }

    private void validatePass() {
        String OldPass = edtOldPass.getText().toString().trim();
        String NewPass = edtNewPass.getText().toString().trim();
        String CPass = edtCOnfirmPass.getText().toString().trim();

        if (TextUtils.isEmpty(OldPass)) {
            edtOldPass.requestFocus();
            edtOldPass.setError("Please enter old pass!");
        }else if (TextUtils.isEmpty(NewPass)) {
            edtNewPass.requestFocus();
            edtNewPass.setError("Please enter New pass!");
        } else if (TextUtils.isEmpty(CPass)) {
            edtCOnfirmPass.requestFocus();
            edtCOnfirmPass.setError("Please enter Confirm pass!");
        } else if (!NewPass.equals(CPass)) {
            MyUtils.showTheToastMessage("Password Don't Match");
        }  else {
            if (MyUtils.isNetworkAvailable()) {
                MyUtils.hideKeyboard(getActivity());
                MyUtils.showProgressDialog(getActivity(), false);
                apicallchangepass(OldPass,NewPass);
            } else {
                MyUtils.showTheToastMessage("Please Check Internet Connection!");
            }
        }

    }

    private void apicallchangepass(String oldPass, String newPass) {

        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            js.put("old_password", oldPass);
            js.put("new_password", newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.CHANGE_PASS_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginMethod", response.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (response.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(response.getString("message"));
                                App.sharedPreferences.setKey(MySharedPreferences.pass, response.getString("password"));
                                handleBackStack();
                                App.sharedPreferences.setKey(MySharedPreferences.isLogin, MySharedPreferences.NO);
                                App.sharedPreferences.setKey(MySharedPreferences.isSubscribe, MySharedPreferences.NO);
                                App.sharedPreferences.ClearAllData();
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
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
}