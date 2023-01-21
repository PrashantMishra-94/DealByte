package com.deal.bytee.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.deal.bytee.Utils.UtilsKt;
import com.deal.bytee.activities.LoginActivity;
import com.deal.bytee.adapter.CartVoucherAdapter;
import com.deal.bytee.adapter.HomeRestaurantAdapter;
import com.deal.bytee.listnerr.RedeemVoucherClick;
import com.deal.bytee.model.BusinessGallaryModel;
import com.deal.bytee.model.MyVoucherModel;
import com.deal.bytee.model.ReviewModel;
import com.deal.bytee.model.VoucherModel;
import com.deal.bytee.model.menuModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CartFragment extends BaseFragment {

    View view;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvCartVoucher;
    private AppCompatImageView ivBack;

    List<MyVoucherModel> myVoucherModelList = new ArrayList<>();


    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    private void initView(View view) {
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        rvCartVoucher = (RecyclerView) view.findViewById(R.id.rvCartVoucher);
        ivBack = (AppCompatImageView) view.findViewById(R.id.ivBack);

    }

    public static final class Companion {
        private Companion() {
        }

        public static final CartFragment instance() {
            return new CartFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackStack();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                initVoucher();
            }
        });
        initVoucher();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initVoucher();
    }

    private void initVoucher() {

        // MyUtils.showProgressDialog(getContext(),false);

        if (!UtilsKt.isLogin()) return;
        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }

        //Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            } else {
                js.put("userid", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.GET_MY_VOUCHERS_LIST, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("detailsss>", obj.toString());
                        myVoucherModelList.clear();
                        // MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                if (obj.has("voucherslist") && !obj.isNull("voucherslist")) {
                                    JSONArray DetailedData = obj.getJSONArray("voucherslist");
                                    for (int i = 0; i < DetailedData.length(); i++) {
                                        JSONObject galarydata = DetailedData.getJSONObject(i);
                                        MyVoucherModel model = new MyVoucherModel();
                                        model.setCv_id(galarydata.getString("cv_id"));
                                        //model.setDiscountamount(galarydata.getInt("discountamount"));
                                        model.setCv_status(galarydata.getString("cv_status"));
                                        model.setCv_code(galarydata.getString("cv_code"));
                                        model.setCv_price(galarydata.getString("cv_price"));
                                        model.setCv_min_purchase_amount(galarydata.getString("cv_min_purchase_amount"));
                                        model.setCv_business_id(galarydata.getString("cv_business_id"));
                                        model.setCv_expiry_date(galarydata.getString("cv_expiry_date"));
                                        model.setCv_business_title(galarydata.getString("cv_business_title"));
                                        //model.setCv_amount_applied(galarydata.getInt("cv_amount_applied"));
                                        //model.setCv_discount(galarydata.getString("cv_discount"));
                                        myVoucherModelList.add(model);
                                    }
                                    rvCartVoucher.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    String name = CartFragment.class.getName();
                                    CartVoucherAdapter cartVoucherAdapter = new CartVoucherAdapter(getActivity(), myVoucherModelList);
                                    cartVoucherAdapter.setRedeemVoucherClick(new RedeemVoucherClick() {
                                        @Override
                                        public void redeemClick(int pos, String amount) {
                                            if (MyUtils.isNetworkAvailable()) {
                                                MyUtils.hideKeyboard(getActivity());
                                                MyUtils.showProgressDialog(getActivity(), false);
                                                apicallForRedeem(myVoucherModelList.get(pos).getCv_code(), amount, myVoucherModelList.get(pos).getCv_business_id());
                                            } else {
                                                MyUtils.showTheToastMessage("Please Check Internet Connection!");
                                            }
                                        }
                                    });
                                    rvCartVoucher.setAdapter(cartVoucherAdapter);
                                    rvCartVoucher.setNestedScrollingEnabled(false);
                                }

                            } else {
                                // MyUtils.showTheToastMessage(obj.getString("message"));
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

    private void apicallForRedeem(String cv_code, String amount, String cv_business_id) {

        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }

        //Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                js.put("vouchercode", cv_code);
                js.put("amounttobeapplied", amount);
                js.put("businessid", cv_business_id);
            } else {
                js.put("userid", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.REDEEM_VOUCHER, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("voucherssss>", obj.toString());
                       // myVoucherModelList.clear();
                         MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                //todo show for payable amount dialog
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText(obj.getString("message")+"\n"+"Payable Amount:Rs."+obj.getInt("applymount"))
                                        .setConfirmText("Done")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.cancel();
                                                //todo api call for buy
                                                initVoucher();
                                            }
                                        })
                                        .show();

                            } else {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Success")
                                        .setContentText(obj.getString("message"))
                                        .setConfirmText("Done")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.cancel();
                                                //todo api call for buy
                                                initVoucher();
                                            }
                                        })
                                        .show();
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