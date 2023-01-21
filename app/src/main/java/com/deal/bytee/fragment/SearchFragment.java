package com.deal.bytee.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
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


public class SearchFragment extends BaseFragment {


    View view;
    private AppCompatEditText edtSearch;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvRestaurant;
    private AppCompatButton btnSearch;
    List<RestaurantModel> mixBusinessList = new ArrayList<>();
    private TabHomeFragment parent;


    public SearchFragment() {
        // Required empty public constructor
        super(R.layout.fragment_search);
    }

    private void initView(View view) {
        edtSearch = (AppCompatEditText) view.findViewById(R.id.edtSearch);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        rvRestaurant = (RecyclerView) view.findViewById(R.id.rvRestaurant);
        btnSearch = (AppCompatButton) view.findViewById(R.id.btnSearch);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final SearchFragment instance() {
            return new SearchFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            this.parent = (TabHomeFragment) parentFragment;
        }
        edtSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //swipe.setVisibility(View.GONE);
                initHomeBusiness();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initHomeBusiness();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);

            }
        });
        return view;
    }

    private void initHomeBusiness() {

        MyUtils.showProgressDialog(getContext(), false);

        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }

        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                js.put("search_keyword", edtSearch.getText().toString().trim());
            } else {
                js.put("userid", "");
                js.put("search_keyword",  edtSearch.getText().toString().trim());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.GET_BUSINESS_LIST, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("homebusinessdata", obj.toString());
                        mixBusinessList.clear();
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
                                        mixBusinessList.add(model);
                                    }
                                    initMixRestaurant();
                                }
                            } else {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                swipe.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipe.setVisibility(View.VISIBLE);
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
    HomeRestaurantAdapter homeRestaurantAdapter;

    private void initMixRestaurant() {
        swipe.setVisibility(View.VISIBLE);
        rvRestaurant.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String name = HomeFragment.class.getName();
        homeRestaurantAdapter = new HomeRestaurantAdapter(getActivity(), name,mixBusinessList);
        rvRestaurant.setAdapter(homeRestaurantAdapter);
        homeRestaurantAdapter.setRestaurantClick(new RestaurantClick() {
            @Override
            public void resClick(int pos) {
                onBusinessDetailsClick(mixBusinessList.get(pos).getId());
            }
        });
        rvRestaurant.setNestedScrollingEnabled(false);

    }

    private void onBusinessDetailsClick(String businessid) {
        /*TabHomeFragment tabHome = this.parent;
        if (tabHome != null) {
            //BaseFragment instance = RestaurantDetailsFragment.Companion.instance(businessid);
            BaseFragment instance = BusinessDetailsFragment.Companion.getInstance(businessid);
            //String name = RestaurantDetailsFragment.class.getName();
            String name = BusinessDetailsFragment.class.getName();
            tabHome.launchChild(instance, true, name);
        }*/
        BusinessActivity.Companion.start(requireActivity(), businessid);
    }

}