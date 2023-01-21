package com.deal.bytee.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.deal.bytee.activities.BusinessActivity;
import com.deal.bytee.adapter.HomeRestaurantAdapter;
import com.deal.bytee.listnerr.RestaurantClick;
import com.deal.bytee.model.RestaurantModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;


public class ShowRestaurantFragment extends BaseFragment {

    View view;
    private AppCompatTextView tvTitle;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvRestaurant;
    HomeRestaurantAdapter homeRestaurantAdapter;
    private TabHomeFragment parent;
    public static String types="";
    List<RestaurantModel> restaurantModelList=new ArrayList<>();

    public ShowRestaurantFragment() {
        // Required empty public constructor
        super(R.layout.fragment_show_restaurant);
    }

    private void initView(View view) {
        tvTitle = (AppCompatTextView) view.findViewById(R.id.tvTitle);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        rvRestaurant = (RecyclerView) view.findViewById(R.id.rvRestaurant);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final ShowRestaurantFragment instance(String type) {
            types = type;
            return new ShowRestaurantFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_restaurant, container, false);
        initView(view);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            this.parent = (TabHomeFragment) parentFragment;
        }
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                initRestaurant();
            }
        });
        initRestaurant();
        return view;
    }

    private void initRestaurant() {


        MyUtils.showProgressDialog(getContext(),false);

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
                js.put("userid","");
                js.put("search_keyword", "");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.GET_BUSINESS_LIST, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("loginMethod", obj.toString());
                        restaurantModelList.clear();
                        MyUtils.dismisProgressDialog();
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
                                        model.setImage(objj.getString("image"));
                                        model.setDescription(MyUtils.stripHtml(objj.getString("description")));
                                        model.setCountry(objj.getString("country"));
                                        model.setCity(objj.getString("city"));
                                        model.setArea(objj.getString("area"));
                                        model.setMain_category(objj.getString("main_category"));
                                        model.setSub_category(objj.getString("sub_category"));
                                        if (types.equals("food")){
                                            if (objj.getString("main_category").equals("Restaurant")){
                                                restaurantModelList.add(model);
                                                tvTitle.setText("Restaurant");
                                            }
                                        }else if (types.equals("meat")){
                                            if (objj.getString("main_category").equals("Meat")){
                                                tvTitle.setText("Meat");
                                                restaurantModelList.add(model);
                                            }
                                        }else if (types.equals("Pubs")){
                                            if (objj.getString("main_category").equals("Pubs")){
                                                restaurantModelList.add(model);
                                                tvTitle.setText("Pubs");
                                            }
                                        }else if (types.equals("Catering")){
                                            if (objj.getString("main_category").equals("Catering")){
                                                restaurantModelList.add(model);
                                                tvTitle.setText("Catering");
                                            }
                                        }
                                    }
                                    rvRestaurant.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    String name = ShowRestaurantFragment.class.getName();
                                    homeRestaurantAdapter = new HomeRestaurantAdapter(getActivity(),name,restaurantModelList);
                                    rvRestaurant.setAdapter(homeRestaurantAdapter);
                                    homeRestaurantAdapter.setRestaurantClick(new RestaurantClick() {
                                        @Override
                                        public void resClick(int pos) {
                                            onResClick(pos);
                                        }
                                    });
                                    rvRestaurant.setNestedScrollingEnabled(false);
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

    private void onResClick(int pos) {
        /*TabHomeFragment tabHome = this.parent;
        if (tabHome != null) {
            //BaseFragment instance = RestaurantDetailsFragment.Companion.instance(restaurantModelList.get(pos).getId());
            BaseFragment instance = BusinessDetailsFragment.Companion.getInstance(restaurantModelList.get(pos).getId());
            //String name = RestaurantDetailsFragment.class.getName();
            String name = BusinessDetailsFragment.class.getName();
            tabHome.launchChild(instance, true, name);
        }*/
        BusinessActivity.Companion.start(requireActivity(), restaurantModelList.get(pos).getId());
    }

}