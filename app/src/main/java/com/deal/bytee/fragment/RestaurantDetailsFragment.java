package com.deal.bytee.fragment;

import static com.deal.bytee.activities.DashBoardActivity.bottomNavigation;
import static com.deal.bytee.activities.DashBoardActivity.tabAdapter;
import static com.deal.bytee.activities.DashBoardActivity.viewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.deal.bytee.R;
import com.deal.bytee.Utils.App;
import com.deal.bytee.Utils.Endpoints;
import com.deal.bytee.Utils.MySharedPreferences;
import com.deal.bytee.Utils.MyUtils;
import com.deal.bytee.Utils.UtilsKt;
import com.deal.bytee.activities.LoginActivity;
import com.deal.bytee.activities.MapActivity;
import com.deal.bytee.activities.PackageActivity;
import com.deal.bytee.activities.PaymentActivity;
import com.deal.bytee.adapter.FacilityAdapter;
import com.deal.bytee.adapter.MenuAdapter;
import com.deal.bytee.adapter.RestaurantGalleryAdapter;
import com.deal.bytee.adapter.ReviewAdapter;
import com.deal.bytee.model.BusinessGallaryModel;
import com.deal.bytee.model.FacilityModel;
import com.deal.bytee.model.OpeningModel;
import com.deal.bytee.model.ReviewModel;
import com.deal.bytee.model.VoucherModel;
import com.deal.bytee.model.menuModel;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RestaurantDetailsFragment extends BaseFragment {

    View view;
    private SwipeRefreshLayout swipe;
    private LinearLayoutCompat llBack;
    private AppCompatTextView tvRestaurantName;
    private AppCompatTextView tvLocation;
    private ScaleRatingBar simpleRatingBar;
    private AppCompatTextView tvReveiws;
    private AppCompatTextView tvDeliveryType;
    private AppCompatTextView tvOpenTime;
    private AppCompatImageView ivUpload;
    private AppCompatImageView ivRate;
    private AppCompatImageView ivLocation;
    private AppCompatImageView ivLike;
    private AppCompatTextView tvGallerytxt;
    private AppCompatTextView tvDiscounttxt;
    private RecyclerView rvRestaurantGallery;
    private AppCompatTextView tvMenutxt;
    private AppCompatTextView tvViewAll;
    private RecyclerView rvMenu;
    private AppCompatTextView tvReviewtxt;
    private RecyclerView rvReview;
    private AppCompatSpinner spSelectVoucher;
    private AppCompatTextView tvBuyVoucher;
    private AppCompatTextView tvRestaurantDetails;
    public static String detail_id = "";
    public static String voucherid = "";
    public boolean isPurchased = false;
    List<BusinessGallaryModel> businessGallaryModelList = new ArrayList<>();
    List<menuModel> menuModelList = new ArrayList<>();
    List<ReviewModel> reviewModelList = new ArrayList<>();
    List<VoucherModel> voucherModelArrayList = new ArrayList<>();
    List<String> voucherNameStrList = new ArrayList<>();

    String spinnerArray[] = {"Select Voucher", "Rs. 40 (Min Shopping Amount: Rs.1000)",
            "Rs. 72 (Min Shopping Amount: Rs.5000)"
            , "Rs. 100 (Min Shopping Amount: Rs.5500)"};
    private RecyclerView rvFacilities;
    List<FacilityModel> facilityModelList = new ArrayList<>();
    List<OpeningModel> openingModelArrayList = new ArrayList<>();
    private RecyclerView rvOpeningHours;
    private AppCompatTextView tvMondayTime;
    private AppCompatTextView tvTuesdayTime;
    private AppCompatTextView tvWednesdayTime;
    private AppCompatTextView tvThursdayTime;
    private AppCompatTextView tvFridayTime;
    private AppCompatTextView tvSaturdayTime;
    private AppCompatTextView tvSundayTime;
    private AppCompatTextView tvBuyVoucherTxt;
    private String VoucherPriceFinal;
    private String VoucherIDFinal = "";
    private String BusinessName = "";
    private String Discount = "";
    private LinearLayoutCompat llVoucher;
    private LinearLayoutCompat llCatering;
    private AppCompatButton btnCall;
    private AppCompatButton btnAskQuote;
    private Button btnGold;
    private Button btnSilver;
    private Button btnBronze;
    private double latitude;
    private double longitude;
    private String goldMenu;
    private String silverMenu;
    private String bronzeMenu;

    public RestaurantDetailsFragment() {
        super(R.layout.fragment_restaurant_details);
    }

    private void initView(View view) {
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        llBack = (LinearLayoutCompat) view.findViewById(R.id.llBack);
        tvRestaurantName = (AppCompatTextView) view.findViewById(R.id.tvRestaurantName);
        tvLocation = (AppCompatTextView) view.findViewById(R.id.tvLocation);
        simpleRatingBar = (ScaleRatingBar) view.findViewById(R.id.simpleRatingBar);
        tvReveiws = (AppCompatTextView) view.findViewById(R.id.tvReveiws);
        tvDeliveryType = (AppCompatTextView) view.findViewById(R.id.tvDeliveryType);
        tvOpenTime = (AppCompatTextView) view.findViewById(R.id.tvOpenTime);
        ivUpload = (AppCompatImageView) view.findViewById(R.id.ivUpload);
        ivRate = (AppCompatImageView) view.findViewById(R.id.ivRate);
        ivLocation = (AppCompatImageView) view.findViewById(R.id.ivLocation);
        ivLike = (AppCompatImageView) view.findViewById(R.id.ivLike);
        tvGallerytxt = (AppCompatTextView) view.findViewById(R.id.tvGallerytxt);
        tvDiscounttxt = (AppCompatTextView) view.findViewById(R.id.tvDiscounttxt);
        rvRestaurantGallery = (RecyclerView) view.findViewById(R.id.rvRestaurantGallery);
        tvMenutxt = (AppCompatTextView) view.findViewById(R.id.tvMenutxt);
        tvViewAll = (AppCompatTextView) view.findViewById(R.id.tvViewAll);
        rvMenu = (RecyclerView) view.findViewById(R.id.rvMenu);
        tvReviewtxt = (AppCompatTextView) view.findViewById(R.id.tvReviewtxt);
        rvReview = (RecyclerView) view.findViewById(R.id.rvReview);
        spSelectVoucher = (AppCompatSpinner) view.findViewById(R.id.spSelectVoucher);
        tvBuyVoucher = (AppCompatTextView) view.findViewById(R.id.tvBuyVoucher);
        tvRestaurantDetails = (AppCompatTextView) view.findViewById(R.id.tvRestaurantDetails);
        rvFacilities = (RecyclerView) view.findViewById(R.id.rvFacilities);
        rvOpeningHours = (RecyclerView) view.findViewById(R.id.rvOpeningHours);
        tvMondayTime = (AppCompatTextView) view.findViewById(R.id.tvMondayTime);
        tvTuesdayTime = (AppCompatTextView) view.findViewById(R.id.tvTuesdayTime);
        tvWednesdayTime = (AppCompatTextView) view.findViewById(R.id.tvWednesdayTime);
        tvThursdayTime = (AppCompatTextView) view.findViewById(R.id.tvThursdayTime);
        tvFridayTime = (AppCompatTextView) view.findViewById(R.id.tvFridayTime);
        tvSaturdayTime = (AppCompatTextView) view.findViewById(R.id.tvSaturdayTime);
        tvSundayTime = (AppCompatTextView) view.findViewById(R.id.tvSundayTime);
        tvBuyVoucherTxt = (AppCompatTextView) view.findViewById(R.id.tvBuyVoucherTxt);
        llVoucher = (LinearLayoutCompat) view.findViewById(R.id.llVoucher);
        llCatering = (LinearLayoutCompat) view.findViewById(R.id.llCatering);
        btnCall = (AppCompatButton) view.findViewById(R.id.btnCall);
        btnAskQuote = (AppCompatButton) view.findViewById(R.id.btnAskQuote);
        btnGold = view.findViewById(R.id.btnGold);
        btnSilver = view.findViewById(R.id.btnSilver);
        btnBronze = view.findViewById(R.id.btnBronze);

        btnGold.setOnClickListener(packageClickListener);
        btnSilver.setOnClickListener(packageClickListener);
        btnBronze.setOnClickListener(packageClickListener);
    }


    public static final class Companion {
        private Companion() {
        }

        public static final RestaurantDetailsFragment instance(String id) {
            detail_id = id;
            Log.d("detail_id>", detail_id);
            return new RestaurantDetailsFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        initView(view);
        llCatering.setVisibility(View.GONE);
        llVoucher.setVisibility(View.GONE);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                apicallForDetails();
            }
        });
        apicallForDetails();
        bindClick();

        return view;
    }

    private void apicallForDetails() {
        MyUtils.showProgressDialog(getContext(), false);

        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        Log.d("detail_id>", detail_id);
        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                js.put("businessid", detail_id);

            } else {
                js.put("userid", "");
                js.put("businessid", detail_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.GET_BUSINESS_DETAILS, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("detailsss>", obj.toString());
                        businessGallaryModelList.clear();
                        menuModelList.clear();
                        reviewModelList.clear();
                        voucherNameStrList.clear();
                        voucherModelArrayList.clear();
                        facilityModelList.clear();
                        openingModelArrayList.clear();
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {

                                if (obj.has("DetailedData") && !obj.isNull("DetailedData")) {
                                    JSONArray DetailedData = obj.getJSONArray("DetailedData");
                                    JSONObject datamain = DetailedData.getJSONObject(0);
                                    if (datamain.getString("main_category").equals("Catering")) {
                                        llCatering.setVisibility(View.VISIBLE);
                                        llVoucher.setVisibility(View.GONE);
                                    } else {
                                        llCatering.setVisibility(View.GONE);
                                        llVoucher.setVisibility(View.VISIBLE);
                                    }
                                    try {
                                        latitude = Double.parseDouble(datamain.getString("lat"));
                                        longitude = Double.parseDouble(datamain.getString("long"));
                                    } catch (NumberFormatException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                    goldMenu = datamain.getString("goldmenu");
                                    silverMenu = datamain.getString("silvermenu");
                                    bronzeMenu = datamain.getString("bronzemenu");
                                    tvRestaurantName.setText(datamain.getString("title"));
                                    BusinessName = datamain.getString("title");
                                    Discount = datamain.getString("discount");
                                    tvDiscounttxt.setText(datamain.getString("discount") + "% Discount");
                                    tvLocation.setText(datamain.getString("area") + ", " + datamain.getString("city"));
                                    simpleRatingBar.setRating(Float.parseFloat(datamain.getString("rate")));
                                    tvReveiws.setText("( " + datamain.getString("review_count") + " Reviews )");
                                    tvDeliveryType.setText(datamain.getString("delivery_type"));
                                    tvRestaurantDetails.setText(datamain.getString("description"));
                                    if (datamain.getInt("voucher_purchased") == 1) {
                                        isPurchased = true;

                                    }
                                    if (datamain.has("open_time") && !datamain.isNull("open_time")) {
                                        JSONArray open_time = datamain.getJSONArray("open_time");
                                        for (int i = 0; i < open_time.length(); i++) {
                                            JSONObject galarydata = open_time.getJSONObject(i);
                                            tvMondayTime.setText(galarydata.getString("Monday"));
                                            tvTuesdayTime.setText(galarydata.getString("Tuesday"));
                                            tvWednesdayTime.setText(galarydata.getString("Wednesday"));
                                            tvThursdayTime.setText(galarydata.getString("Thursday"));
                                            tvFridayTime.setText(galarydata.getString("Friday"));
                                            tvSaturdayTime.setText(galarydata.getString("Saturday"));
                                            tvSundayTime.setText(galarydata.getString("Sunday"));
                                        }

                                    }

                                }

                                if (obj.has("morepics") && !obj.isNull("morepics")) {
                                    JSONArray DetailedData = obj.getJSONArray("morepics");
                                    for (int i = 0; i < DetailedData.length(); i++) {
                                        String galarydata = DetailedData.getString(i);
                                        BusinessGallaryModel model = new BusinessGallaryModel();
                                        model.setName("");
                                        model.setImage(String.valueOf(galarydata));
                                        businessGallaryModelList.add(model);
                                    }
                                    initGallery();
                                }


                                if (obj.has("facilities") && !obj.isNull("facilities")) {
                                    JSONArray DetailedData = obj.getJSONArray("facilities");
                                    for (int i = 0; i < DetailedData.length(); i++) {
                                        JSONObject galarydata = DetailedData.getJSONObject(i);
                                        FacilityModel model = new FacilityModel();
                                        model.setName(galarydata.getString("fac_title"));
                                        model.setImageurl(galarydata.getString("image"));
                                        facilityModelList.add(model);
                                    }
                                    initFacility();
                                }

                                if (obj.has("menu") && !obj.isNull("menu")) {
                                    JSONArray DetailedData = obj.getJSONArray("menu");
                                    for (int i = 0; i < DetailedData.length(); i++) {
                                        JSONObject galarydata = DetailedData.getJSONObject(i);
                                        menuModel model = new menuModel();
                                        model.setName(galarydata.getString("m_title"));
                                        model.setCount("Rs." + galarydata.getString("m_price"));
                                        menuModelList.add(model);
                                    }
                                    initMenu();
                                }

                                if (obj.has("reviews") && !obj.isNull("reviews")) {
                                    JSONArray DetailedData = obj.getJSONArray("reviews");
                                    for (int i = 0; i < DetailedData.length(); i++) {
                                        JSONObject galarydata = DetailedData.getJSONObject(i);
                                        ReviewModel model = new ReviewModel();
                                        model.setReview_id(galarydata.getString("review_id"));
                                        model.setReviewer_name(galarydata.getString("reviewer_name"));
                                        model.setReviewer_image(galarydata.getString("reviewer_image"));
                                        model.setRating(galarydata.getString("rating"));
                                        model.setReview_text(galarydata.getString("review_text"));
                                        model.setTime(galarydata.getString("time"));
                                        reviewModelList.add(model);
                                    }
                                    initReview();
                                }

                                if (obj.has("vouchers") && !obj.isNull("vouchers")) {
                                    JSONArray DetailedData = obj.getJSONArray("vouchers");
                                    for (int i = 0; i < DetailedData.length(); i++) {
                                        JSONObject galarydata = DetailedData.getJSONObject(i);
                                        tvBuyVoucherTxt.setText("₹" + DetailedData.getJSONObject(0).getString("v_price"));
                                        VoucherPriceFinal = DetailedData.getJSONObject(0).getString("v_price");
                                        VoucherIDFinal = DetailedData.getJSONObject(0).getString("v_id");
                                        Log.d("VoucherIDFinal>>>", VoucherIDFinal);
                                        VoucherModel model = new VoucherModel();
                                        model.setV_id(galarydata.getString("v_id"));
                                        model.setV_title(galarydata.getString("v_title"));
                                        model.setV_price(galarydata.getString("v_price"));
                                        model.setV_minimum_purchase_amount(galarydata.getString("v_minimum_purchase_amount"));
                                        model.setV_discount(galarydata.getString("v_discount"));
                                        voucherModelArrayList.add(model);
                                        voucherNameStrList.add("Rs. " + galarydata.getString("v_price"));
                                        //Rs. 40 (Min Shopping Amount: Rs.1000)
                                    }
                                    setSpinnerVoucher();
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


    private void initFacility() {
        rvFacilities.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        String name = RestaurantDetailsFragment.class.getName();
        rvFacilities.setAdapter(new FacilityAdapter(getActivity(), facilityModelList));
        rvFacilities.setNestedScrollingEnabled(false);
    }

    private void setSpinnerVoucher() {

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                R.layout.lyt_spinner_text,
                voucherNameStrList);
        spSelectVoucher.setAdapter(spinnerArrayAdapter);
        spSelectVoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                voucherid = voucherModelArrayList.get(position).getV_id();
               /* for (int i = 0; i < voucherModelArrayList.size(); i++) {
                    if (voucherNameStrList.get(position).contains(voucherModelArrayList.get(i).getV_price())) {
                        voucherid = voucherModelArrayList.get(i).getV_id();
                        break;
                    }
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void bindClick() {

        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackStack();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+917795176850"));
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
                } else {
                    startActivity(intent);
                }
            }
        });

        String finalIslogin = islogin;
        ivRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalIslogin.equals(MySharedPreferences.YES)) {
                    //todo ask for rating
                    showDialogToAskForRateReview();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    //  MyUtils.showTheToastMessage("Please login to use features!");
                }
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalIslogin.equals(MySharedPreferences.YES)) {
                    apicallAddFavourite();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    // MyUtils.showTheToastMessage("Please login to use features!");
                }
            }
        });

        tvBuyVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalIslogin.equals(MySharedPreferences.YES)) {
                    if (isPurchased){
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Purchased")
                                .setContentText("You're Already Purchased!")
                                .setConfirmText("OKAY")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .show();
                    }else {
                        //new payment activity
                        if (VoucherIDFinal.isEmpty() && VoucherIDFinal == "") {
                            MyUtils.showTheToastMessage("Please select voucher");
                        } else {
                            Intent intent = new Intent(getActivity(), PaymentActivity.class);
                            intent.putExtra("businessname",BusinessName);
                            intent.putExtra("discount",Discount);
                            intent.putExtra("VoucherPriceFinal",VoucherPriceFinal);
                            intent.putExtra("VoucherIDFinal",VoucherIDFinal);
                            intent.putExtra("BusinessId",detail_id);
                            startActivity(intent);
                        }
                    }

                } else {
                    //  MyUtils.showTheToastMessage("Please login to use features!");
                    App.sharedPreferences.setKey(MySharedPreferences.isFromDetails, MySharedPreferences.YES);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("businessname",BusinessName);
                    intent.putExtra("discount",Discount);
                    intent.putExtra("VoucherPriceFinal",VoucherPriceFinal);
                    intent.putExtra("VoucherIDFinal",VoucherIDFinal);
                    intent.putExtra("BusinessId",detail_id);
                    startActivity(intent);
                   // startActivity(new Intent(getActivity(), LoginActivity.class));
                }


            }
        });

        btnAskQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForQuoteForm();
            }
        });

        ivLocation.setOnClickListener(v -> {
            if (latitude > 0 && longitude > 0) {
                Intent intent = new Intent(requireActivity(), MapActivity.class);
                intent.putExtra(MapActivity.LATITUDE, latitude);
                intent.putExtra(MapActivity.LONGITUDE, longitude);
                intent.putExtra(MapActivity.TITLE, BusinessName);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void askForQuoteForm() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Ask For Quote");


        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(20, 20, 20, 20);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText namee = new EditText(context);
        namee.setHint("Enter Name");
        layout.addView(namee); // Notice this is an add method

        final EditText contact = new EditText(context);
        contact.setHint("Enter Contact Number");
        contact.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(contact); // Another add method


        final EditText comment = new EditText(context);
        comment.setHint("Enter Comment");
        comment.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        layout.addView(comment);


        builder.setView(layout);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nameitem = namee.getText().toString().trim();
                String contactt = contact.getText().toString().trim();
                String commentt = comment.getText().toString().trim();

                if (TextUtils.isEmpty(nameitem)) {
                    namee.requestFocus();
                    namee.setError("Please enter name");
                } else if (!MyUtils.isValidMobile(contactt)) {
                    contact.requestFocus();
                    contact.setError("Please enter contact");
                } else if (TextUtils.isEmpty(commentt)) {
                    comment.requestFocus();
                    comment.setError("Please enter comment");
                } else {
                    if (MyUtils.isNetworkAvailable()) {
                        MyUtils.hideKeyboard(getActivity());
                        MyUtils.showProgressDialog(getActivity(), false);
                         apicalladdQuote(nameitem,contactt,commentt);
                    } else {
                        MyUtils.showTheToastMessage("Please Check Internet Connection!");
                    }
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    private void apicalladdQuote(String nameitem, String contactt, String commentt) {


        MyUtils.showProgressDialog(getContext(), false);
        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        Log.d("detail_id>", detail_id);

        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                js.put("businessid", detail_id);
                js.put("name", nameitem);
                js.put("mobile", contactt);
                js.put("email", "");
                js.put("address", "");
                js.put("query", commentt);
            } else {
                js.put("userid", "");
                js.put("businessid", detail_id);
                js.put("name", nameitem);
                js.put("mobile", contactt);
                js.put("email", "");
                js.put("address", "");
                js.put("query", commentt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.ADD_QUOTE, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("ADD_QUOTE>>>", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //MyUtils.showTheToastMessage(obj.getString("message"));
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Done!")
                                        .setContentText(obj.getString("message")+"\n")
                                        .setConfirmText("OKAY")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.cancel();
                                            }
                                        })
                                        .show();
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

    private void showDialogToAskForRateReview() {

        Dialog dialog = new Dialog(getActivity(), R.style.ChatDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lyt_rate_review);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ScaleRatingBar ratingBar = dialog.findViewById(R.id.simpleRatingBar);
        AppCompatEditText edtReview = dialog.findViewById(R.id.edtReview);
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = edtReview.getText().toString().trim();

                if (TextUtils.isEmpty(review)) {
                    edtReview.requestFocus();
                    edtReview.setError("Please enter review");
                } else {
                    if (MyUtils.isNetworkAvailable()) {
                        dialog.dismiss();
                        MyUtils.hideKeyboard(getActivity());
                        MyUtils.showProgressDialog(getActivity(), false);
                        apicalladdrate(ratingBar.getRating(), review);
                    } else {
                        dialog.dismiss();
                        MyUtils.showTheToastMessage("Please Check Internet Connection!");
                    }
                }
            }
        });
        window.setAttributes(lp);
        dialog.show();

    }

    private void apicalladdrate(float rating, String review) {

        MyUtils.showProgressDialog(getContext(), false);
        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        Log.d("detail_id>", detail_id);

        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                js.put("businessid", detail_id);
                js.put("rating", String.valueOf(rating));
                js.put("review", review);
            } else {
                js.put("userid", "");
                js.put("businessid", detail_id);
                js.put("rating", String.valueOf(rating));
                js.put("review", review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.ADD_RATE_REVIEW, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("ADD_RATE_REVIEW>>>", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //MyUtils.showTheToastMessage(obj.getString("message"));
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Done!")
                                        .setContentText("Thank You For Your Rate & Review!\n")
                                        .setConfirmText("OKAY")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.cancel();
                                                apicallForDetails();
                                            }
                                        })
                                        .show();
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


    private void apicallAddFavourite() {

        MyUtils.showProgressDialog(getContext(), false);
        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        Log.d("detail_id>", detail_id);
        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                js.put("businessid", detail_id);

            } else {
                js.put("userid", "");
                js.put("businessid", detail_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.ADD_WISHLIST, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("detailsss>", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                viewPager.setCurrentItem(3);
                                bottomNavigation.setSelectedItemId(R.id.nav_favourite);
                                tabAdapter.notifyDataSetChanged();
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

    private void initReview() {
        rvReview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String name = RestaurantDetailsFragment.class.getName();
        rvReview.setAdapter(new ReviewAdapter(getActivity(), reviewModelList));
        rvReview.setNestedScrollingEnabled(false);
    }

    private void initGallery() {
        rvRestaurantGallery.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        String name = RestaurantDetailsFragment.class.getName();
        rvRestaurantGallery.setAdapter(new RestaurantGalleryAdapter(getActivity(), businessGallaryModelList));
        rvRestaurantGallery.setNestedScrollingEnabled(false);
    }

    private void initMenu() {
        rvMenu.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String name = RestaurantDetailsFragment.class.getName();
        rvMenu.setAdapter(new MenuAdapter(getActivity(), menuModelList));
        rvMenu.setNestedScrollingEnabled(false);
    }

    private final View.OnClickListener packageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnGold:
                    openPackage(goldMenu);
                    break;
                case R.id.btnSilver:
                    openPackage(silverMenu);
                    break;
                case R.id.btnBronze:
                    openPackage(bronzeMenu);
                    break;
            }
        }

        private void openPackage(String url) {
            if (UtilsKt.isAllEmpty(url)) {
                Toast.makeText(getActivity(), "Package not found", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(requireActivity(), PackageActivity.class);
            intent.putExtra(PackageActivity.PACKAGE_URL, url);
            startActivity(intent);
        }
    };

}