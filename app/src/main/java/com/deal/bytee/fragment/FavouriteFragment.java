package com.deal.bytee.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
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
import com.deal.bytee.activities.BusinessActivity;
import com.deal.bytee.adapter.CartVoucherAdapter;
import com.deal.bytee.adapter.HomeRestaurantAdapter;
import com.deal.bytee.listnerr.RestaurantClick;
import com.deal.bytee.listnerr.RestaurantRemoveClick;
import com.deal.bytee.model.MyVoucherModel;
import com.deal.bytee.model.RestaurantModel;
import com.deal.bytee.model.WishListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.jvm.internal.Intrinsics;


public class FavouriteFragment extends BaseFragment {

    View view;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvRestaurant;
    HomeRestaurantAdapter homeRestaurantAdapter;
    private TabFavouriteFragment parent;
    private AppCompatImageView ivBack;

    List<RestaurantModel> mixBusinessList = new ArrayList<>();
    List<WishListModel> wishListModelList = new ArrayList<>();
    List<String> businessidlist = new ArrayList<>();


    public FavouriteFragment() {
        super(R.layout.fragment_favourite);
    }

    private void initView(View view) {
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        rvRestaurant = (RecyclerView) view.findViewById(R.id.rvRestaurant);
        ivBack = (AppCompatImageView) view.findViewById(R.id.ivBack);

    }


    public static final class Companion {
        private Companion() {
        }

        public static final FavouriteFragment instance() {
            return new FavouriteFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        initView(view);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackStack();
            }
        });
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            this.parent = (TabFavouriteFragment) parentFragment;
        }
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                initWishlist();
            }
        });

        initWishlist();
        return view;
    }



    private void initWishlist() {

      //  MyUtils.showProgressDialog(getContext(),false);
        if (!UtilsKt.isLogin()) return;
        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            } else {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.GET_MY_WISH_LIST, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("wishlist>", obj.toString());
                        businessidlist.clear();
                        wishListModelList.clear();
                       // MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                if (obj.has("my_wishlist") && !obj.isNull("my_wishlist")) {
                                    JSONArray DetailedData = obj.getJSONArray("my_wishlist");
                                    for (int i = 0; i < DetailedData.length(); i++) {
                                        JSONObject galarydata = DetailedData.getJSONObject(i);
                                        WishListModel model = new WishListModel();
                                        model.setWl_id(galarydata.getString("w_id"));
                                        model.setBusiness_id(galarydata.getString("businessid"));
                                        businessidlist.add(galarydata.getString("businessid"));
                                        wishListModelList.add(model);
                                    }
                                   initHomeBusiness();
                                }
                            } else {
                                rvRestaurant.setVisibility(View.GONE);
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

    private void initRestaurant() {
        rvRestaurant.setVisibility(View.VISIBLE);
        rvRestaurant.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String name = FavouriteFragment.class.getName();
        homeRestaurantAdapter = new HomeRestaurantAdapter(getActivity(), name,mixBusinessList);
        rvRestaurant.setAdapter(homeRestaurantAdapter);
        homeRestaurantAdapter.setRestaurantClick(new RestaurantClick() {
            @Override
            public void resClick(int pos) {
                onBusinessDetailsClick(mixBusinessList.get(pos).getId());
            }
        });
        homeRestaurantAdapter.setRestaurantRemoveClick(new RestaurantRemoveClick() {
            @Override
            public void resClick(int pos) {
                askForDelete(mixBusinessList.get(pos).getId());
            }
        });
        rvRestaurant.setNestedScrollingEnabled(false);
    }

    private void askForDelete(String id) {

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete")
                .setContentText("Are you sure want to Delete?")
                .setCancelText("Cancel")
                .setConfirmText("Yes")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();

                        MyUtils.showProgressDialog(getActivity(), false);

                        String islogin = "";
                        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
                            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
                        }

                        JSONObject js = new JSONObject();
                        try {
                            if (islogin.equals(MySharedPreferences.YES)) {
                                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                                js.put("businessid", id);
                            } else {
                                js.put("userid", "");
                                js.put("businessid", "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.REMOVE_FAVOURITE, js,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject obj) {
                                        Log.d("businesslist", obj.toString());
                                        MyUtils.dismisProgressDialog();
                                        try {
                                            if (obj.getInt("status") == 1) {
                                                MyUtils.showTheToastMessage(obj.getString("message"));
                                                initWishlist();
                                            } else {
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
                })
                .show();
    }

    private void onBusinessDetailsClick(String businessid) {
        /*TabFavouriteFragment tabHome = this.parent;
        if (tabHome != null) {
            //BaseFragment instance = RestaurantDetailsFragment.Companion.instance(businessid);
            BaseFragment instance = BusinessDetailsFragment.Companion.getInstance(businessid);
            //String name = RestaurantDetailsFragment.class.getName();
            String name = BusinessDetailsFragment.class.getName();
            tabHome.launchChild(instance, true, name);
        }*/
        BusinessActivity.Companion.start(requireActivity(), businessid);
    }

    public void initHomeBusiness() {

      //  MyUtils.showProgressDialog(getContext(), false);

        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }

        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                js.put("search_keyword", "");
            } else {
                js.put("userid", "");
                js.put("search_keyword", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.GET_BUSINESS_LIST, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("favourite", obj.toString());
                        mixBusinessList.clear();
                       // MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                // MyUtils.showTheToastMessage(obj.getString("message"));
                                if (obj.has("businesslist") && !obj.isNull("businesslist")) {
                                    JSONArray ankanagalu = obj.getJSONArray("businesslist");
                                    for (int i = 0; i < ankanagalu.length(); i++) {
                                        JSONObject objj = ankanagalu.getJSONObject(i);

                                        RestaurantModel model = new RestaurantModel();
                                        model.setId(objj.getString("id"));
                                        model.setTitle(objj.getString("title"));
                                        model.setDiscount(objj.getString("discount"));
                                        model.setImage(objj.getString("image"));
                                        model.setDescription(MyUtils.stripHtml(objj.getString("description")));
                                        model.setCountry(objj.getString("country"));
                                        model.setCity(objj.getString("city"));
                                        model.setArea(objj.getString("area"));
                                        model.setMain_category(objj.getString("main_category"));
                                        model.setSub_category(objj.getString("sub_category"));

                                        for (String businessid : businessidlist){
                                            if (businessid.equals(objj.getString("id"))){
                                                mixBusinessList.add(model);
                                            }
                                        }
                                    }
                                    initRestaurant();
                                }
                            } else {
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




}