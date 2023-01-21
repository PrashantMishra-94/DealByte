package com.deal.bytee.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.*
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.deal.bytee.R
import com.deal.bytee.Utils.*
import com.deal.bytee.activities.BusinessActivity
import com.deal.bytee.activities.BusinessActivity.Companion.start
import com.deal.bytee.adapter.HomeNewOfferAdapter
import com.deal.bytee.adapter.HomeOfferAdapter
import com.deal.bytee.adapter.HomeRestaurantAdapter
import com.deal.bytee.model.RestaurantModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.internal.Intrinsics

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val REQUEST_CHECK_SETTINGS = 100

    private var swipe: SwipeRefreshLayout? = null
    private var nestedScrollView: NestedScrollView? = null
    private var tvFilter: AppCompatTextView? = null
    private var tvCancel: AppCompatTextView? = null
    private var rvOfferImage: RecyclerView? = null
    private var llFood: LinearLayoutCompat? = null
    private var llSearch: LinearLayoutCompat? = null
    private var llSalonSpa: LinearLayoutCompat? = null
    private var llCatering: LinearLayoutCompat? = null
    private var llBarspub: LinearLayoutCompat? = null
    private var llRemoveOnSearch: LinearLayoutCompat? = null
    private var rvNearestPubs: RecyclerView? = null
    private var rvTopRestaurant: RecyclerView? = null
    private var rvMixRestaurant: RecyclerView? = null
    private var parent: TabHomeFragment? = null
    private var llLocation: LinearLayoutCompat? = null
    var homeRestaurantAdapter: HomeRestaurantAdapter? = null
    private var spCityArea: AppCompatSpinner? = null
    private var spCategory: AppCompatSpinner? = null
    private var btnSearch: AppCompatButton? = null
    private var edtSearch: AppCompatEditText? = null
    private var rvDiscountGroceries: RecyclerView? = null
    private var rvFreshMeat: RecyclerView? = null
    var nearestPubList: MutableList<RestaurantModel> = ArrayList()
    var topRestaurantList: MutableList<RestaurantModel> = ArrayList()
    var discounongroceryList: MutableList<RestaurantModel> = ArrayList()
    var freshMeatList: MutableList<RestaurantModel> = ArrayList()
    var mixBusinessList: MutableList<RestaurantModel> = ArrayList()
    var mixSearchBusinessList: MutableList<RestaurantModel> = ArrayList()
    private var mShimmerViewContainer: ShimmerFrameLayout? = null
    private lateinit var permissionUtils: PermissionUtils
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private fun initView(view: View) {
        swipe = view.findViewById<View>(R.id.swipe) as SwipeRefreshLayout
        nestedScrollView = view.findViewById<View>(R.id.nestedScrollView) as NestedScrollView
        tvFilter = view.findViewById<View>(R.id.tvFilter) as AppCompatTextView
        tvCancel = view.findViewById<View>(R.id.tvCancel) as AppCompatTextView
        rvOfferImage = view.findViewById<View>(R.id.rvOfferImage) as RecyclerView
        llFood = view.findViewById<View>(R.id.llFood) as LinearLayoutCompat
        llSearch = view.findViewById<View>(R.id.llSearch) as LinearLayoutCompat
        llSalonSpa = view.findViewById<View>(R.id.llSalonSpa) as LinearLayoutCompat
        llCatering = view.findViewById<View>(R.id.llCatering) as LinearLayoutCompat
        llBarspub = view.findViewById<View>(R.id.llBarspub) as LinearLayoutCompat
        llRemoveOnSearch = view.findViewById<View>(R.id.llRemoveOnSearch) as LinearLayoutCompat
        rvNearestPubs = view.findViewById<View>(R.id.rvNewOffer) as RecyclerView
        rvTopRestaurant = view.findViewById<View>(R.id.rvSpecialVoucher) as RecyclerView
        rvMixRestaurant = view.findViewById<View>(R.id.rvRestaurant) as RecyclerView
        llLocation = view.findViewById<View>(R.id.llLocation) as LinearLayoutCompat
        tvAddress = view.findViewById<View>(R.id.tvAddress) as AppCompatTextView
        spCityArea = view.findViewById<View>(R.id.spCityArea) as AppCompatSpinner
        spCategory = view.findViewById<View>(R.id.spCategory) as AppCompatSpinner
        btnSearch = view.findViewById<View>(R.id.btnSearch) as AppCompatButton
        edtSearch = view.findViewById<View>(R.id.edtSearch) as AppCompatEditText
        rvDiscountGroceries = view.findViewById<View>(R.id.rvDiscountGroceries) as RecyclerView
        rvFreshMeat = view.findViewById<View>(R.id.rvFreshMeat) as RecyclerView
        mShimmerViewContainer =
            view.findViewById<View>(R.id.mShimmerViewContainer) as ShimmerFrameLayout
        var islogin = ""
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
        }
        if (islogin == MySharedPreferences.YES) {
            tvAddress!!.setText(App.sharedPreferences.getKey(MySharedPreferences.address))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
        permissionUtils = PermissionUtils(this) { init() }
        permissionUtils.getPermissions()
        initView(requireView())
        mShimmerViewContainer!!.visibility = View.VISIBLE
        mShimmerViewContainer!!.startShimmer()
        swipe!!.visibility = View.GONE
        val parentFragment = parentFragment
        if (parentFragment != null) {
            parent = parentFragment as TabHomeFragment?
        }
        swipe!!.setOnRefreshListener {
            swipe!!.isRefreshing = false
            initOffer()
            init()
        }
        bindClick()
        initOffer()
        //init()
        edtSearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //swipe.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(s)) {
                    llRemoveOnSearch!!.visibility = View.GONE
                    initSearchBusiness()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> init()
                Activity.RESULT_CANCELED -> {
                    AlertDialog.Builder(requireContext(), R.style.Theme_AppCompat_Dialog_Alert).apply {
                        setMessage("Please enable location to get vendor near you")
                        setTitle("Alert")
                        setPositiveButton("Ok") { _, _ ->
                            init()
                        }
                        setNegativeButton("Close") { _, _ ->
                            requireActivity().finishAffinity()
                        }
                        setCancelable(false)
                    }.create().show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun init() {
        if (isInternetAvailable(requireActivity())) {
            fusedLocationClient!!.lastLocation.continueWith {
                if (it.isSuccessful && it.result != null) {
                    //Toast.makeText(requireContext(), "Found last location", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Found last location")
                    initHomeBusiness(it.result)
                } else {
                    findLocation()
                }
                null
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun findLocation() {
        Log.d(TAG, "Find Location")
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            Log.d(TAG, locationSettingsResponse.toString())
            if (activity != null) {
                fusedLocationClient!!.requestLocationUpdates(
                    locationRequest,
                    locationCallback, Looper.getMainLooper()
                )
            }
        }

        task.addOnFailureListener { exception ->
            Log.d(TAG, exception.toString())
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                if (activity == null) return@addOnFailureListener
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    startIntentSenderForResult(exception.resolution.intentSender, REQUEST_CHECK_SETTINGS,
                        null, 0, 0, 0, null)
                    /*exception.startResolutionForResult(requireActivity(),
                        REQUEST_CHECK_SETTINGS)*/
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (activity == null) return
            if (locationResult?.locations.isNullOrEmpty()) {
                Toast.makeText(
                    requireActivity(), "Not able to find your current location",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            Log.d(TAG, "${locationResult!!.locations.size} locations found")
            initHomeBusiness(locationResult.locations[0])
            //Toast.makeText(requireContext(), "Found location", Toast.LENGTH_SHORT).show()
            removeLocationUpdates()
        }
    }

    private fun removeLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    private fun initHomeBusiness(location: Location) {
        mShimmerViewContainer!!.visibility = View.VISIBLE
        mShimmerViewContainer!!.startShimmer()
        var islogin = ""
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
        }
        val js = JSONObject()
        try {
            if (islogin == MySharedPreferences.YES) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id))
            } else {
                js.put("userid", "")
            }
            js.put("search_keyword", "")
            js.put("lat", location.latitude)
            js.put("long", location.longitude)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request = JsonObjectRequest(
            Request.Method.POST, Endpoints.GET_LOCATION_BASED_BUSINESS_LISTING, js,
            { obj ->
                Log.d("homebusinessdata", obj.toString())
                nearestPubList.clear()
                topRestaurantList.clear()
                discounongroceryList.clear()
                freshMeatList.clear()
                mixBusinessList.clear()
                //  MyUtils.dismisProgressDialog();
                try {
                    if (obj.getInt("status") == 1) {
                        // MyUtils.showTheToastMessage(obj.getString("message"));
                        if (obj.has("businesslist") && !obj.isNull("businesslist")) {
                            val ankanagalu = obj.getJSONArray("businesslist")
                            for (i in 0 until ankanagalu.length()) {
                                val objj = ankanagalu.getJSONObject(i)
                                val model = RestaurantModel()
                                model.id = objj.getString("id")
                                model.title = objj.getString("title")
                                model.discount = objj.getString("discount")
                                model.image = objj.getString("image")
                                model.description = MyUtils.stripHtml(objj.getString("description"))
                                model.country = objj.getString("country")
                                model.city = objj.getString("city")
                                model.area = objj.getString("area")
                                model.main_category = objj.getString("main_category")
                                model.sub_category = objj.getString("sub_category")
                                if (objj.getString("main_category") == "Pubs") {
                                    nearestPubList.add(model)
                                } else if (objj.getString("main_category") == "Meat") {
                                    freshMeatList.add(model)
                                } else if (objj.getString("main_category") == "Restaurant") {
                                    topRestaurantList.add(model)
                                } else if (objj.getString("main_category") == "Catering") {
                                    discounongroceryList.add(model)
                                }
                                mixBusinessList.add(model)
                            }
                            initNearestPubs()
                            initDiscountGrocery()
                            initFreshMeat()
                            initTopRestaurant()
                            initMixRestaurant(false)
                        }
                    } else {
                        MyUtils.showTheToastMessage(obj.getString("message"))
                        mShimmerViewContainer!!.visibility = View.GONE
                        mShimmerViewContainer!!.stopShimmer()
                        swipe!!.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    mShimmerViewContainer!!.visibility = View.GONE
                    mShimmerViewContainer!!.stopShimmer()
                    swipe!!.visibility = View.VISIBLE
                }
            }) { error ->
            MyUtils.dismisProgressDialog()
            val error_type = MyUtils.simpleVolleyRequestError("TAG", error)
            MyUtils.showTheToastMessage(error_type)
        }
        request.retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        App.mRequestQue.add(request)
    }

    private fun bindClick() {
        tvCancel!!.setOnClickListener { //onSearchClick();
            initMixRestaurant(false)
            llRemoveOnSearch!!.visibility = View.VISIBLE
            edtSearch!!.setText("")
            edtSearch!!.hint = "Search"
        }
        tvFilter!!.setOnClickListener { onFilterClick() }
        llFood!!.setOnClickListener { onFoodClick("food") }
        llSalonSpa!!.setOnClickListener { onFoodClick("meat") }
        llCatering!!.setOnClickListener { onFoodClick("Catering") }
        llBarspub!!.setOnClickListener { onFoodClick("Pubs") }
    }

    private fun initSearchBusiness() {

        // MyUtils.showProgressDialog(getContext(), false);
        var islogin = ""
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
        }
        val js = JSONObject()
        try {
            if (islogin == MySharedPreferences.YES) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id))
                js.put("search_keyword", edtSearch!!.text.toString().trim { it <= ' ' })
            } else {
                js.put("userid", "")
                js.put("search_keyword", edtSearch!!.text.toString().trim { it <= ' ' })
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request = JsonObjectRequest(
            Request.Method.POST, Endpoints.GET_BUSINESS_LIST, js,
            { obj ->
                Log.d("homebusinessdata", obj.toString())
                mixSearchBusinessList.clear()
                //  MyUtils.dismisProgressDialog();
                try {
                    if (obj.getInt("status") == 1) {
                        // MyUtils.showTheToastMessage(obj.getString("message"));
                        if (obj.has("businesslist") && !obj.isNull("businesslist")) {
                            val ankanagalu = obj.getJSONArray("businesslist")
                            for (i in 0 until ankanagalu.length()) {
                                val objj = ankanagalu.getJSONObject(i)
                                val model = RestaurantModel()
                                model.id = objj.getString("id")
                                model.title = objj.getString("title")
                                model.discount = objj.getString("discount")
                                model.image = objj.getString("image")
                                model.description = MyUtils.stripHtml(objj.getString("description"))
                                model.country = objj.getString("country")
                                model.city = objj.getString("city")
                                model.area = objj.getString("area")
                                model.main_category = objj.getString("main_category")
                                model.sub_category = objj.getString("sub_category")
                                mixSearchBusinessList.add(model)
                            }
                            initMixRestaurant(true)
                        }
                    } else {
                        MyUtils.showTheToastMessage(obj.getString("message"))
                        swipe!!.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    swipe!!.visibility = View.VISIBLE
                }
            }) { error ->
            MyUtils.dismisProgressDialog()
            val error_type = MyUtils.simpleVolleyRequestError("TAG", error)
            MyUtils.showTheToastMessage(error_type)
        }
        request.retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        App.mRequestQue.add(request)
    }

    private fun onFoodClick(type: String) {
        val tabHome = parent
        if (tabHome != null) {
            val instance: BaseFragment = ShowRestaurantFragment.Companion.instance(type)
            val name = ShowRestaurantFragment::class.java.name
            Intrinsics.checkExpressionValueIsNotNull(
                name,
                "ShowRestaurantFragment::class.java.name"
            )
            tabHome.launchChild(instance, true, name)
        }
    }

    private fun onFilterClick() {
        val tabHome = parent
        if (tabHome != null) {
            val instance: BaseFragment = FilterFragment.Companion.instance()
            val name = FilterFragment::class.java.name
            Intrinsics.checkExpressionValueIsNotNull(name, "FilterFragment::class.java.name")
            tabHome.launchChild(instance, true, name)
        }
    }

    private fun onSearchClick() {
        val tabHome = parent
        if (tabHome != null) {
            val instance: BaseFragment = SearchFragment.Companion.instance()
            val name = SearchFragment::class.java.name
            Intrinsics.checkExpressionValueIsNotNull(name, "SearchFragment::class.java.name")
            tabHome.launchChild(instance, true, name)
        }
    }

    private fun initMixRestaurant(isFromSearch: Boolean) {
        mShimmerViewContainer!!.visibility = View.GONE
        mShimmerViewContainer!!.stopShimmer()
        swipe!!.visibility = View.VISIBLE
        Log.d("search>>>>>>>>", isFromSearch.toString())
        if (!isFromSearch) {
            llRemoveOnSearch!!.visibility = View.VISIBLE
        }
        rvMixRestaurant!!.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val name = HomeFragment::class.java.name
        homeRestaurantAdapter = if (isFromSearch) {
            HomeRestaurantAdapter(activity, name, mixSearchBusinessList)
        } else {
            HomeRestaurantAdapter(activity, name, mixBusinessList)
        }
        rvMixRestaurant!!.adapter = homeRestaurantAdapter
        homeRestaurantAdapter!!.setRestaurantClick { pos ->
            if (isFromSearch) {
                onBusinessDetailsClick(mixSearchBusinessList[pos].id)
            } else {
                onBusinessDetailsClick(mixBusinessList[pos].id)
            }
        }
        rvMixRestaurant!!.isNestedScrollingEnabled = false
    }

    public fun onBusinessDetailsClick(businessid: String) {
        /*val tabHome = parent
        if (tabHome != null) {
            //val instance: BaseFragment = RestaurantDetailsFragment.Companion.instance(businessid)
            val instance: BaseFragment = BusinessDetailsFragment.getInstance(businessid)
            //val name = RestaurantDetailsFragment::class.java.name
            val name = BusinessDetailsFragment::class.java.name
            tabHome.launchChild(instance, true, name)
        }*/
        start(requireActivity(), businessid)
    }

    private fun initFreshMeat() {
        rvFreshMeat!!.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val name = HomeFragment::class.java.name
        val homeNewOfferAdapter = HomeNewOfferAdapter(activity, freshMeatList, "meat")
        rvFreshMeat!!.adapter = homeNewOfferAdapter
        homeNewOfferAdapter.setRestaurantClick { pos -> onBusinessDetailsClick(freshMeatList[pos].id) }
        rvFreshMeat!!.isNestedScrollingEnabled = false
    }

    private fun initDiscountGrocery() {
        rvDiscountGroceries!!.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val name = HomeFragment::class.java.name
        val homeNewOfferAdapter = HomeNewOfferAdapter(activity, discounongroceryList, "Catering")
        rvDiscountGroceries!!.adapter = homeNewOfferAdapter
        homeNewOfferAdapter.setRestaurantClick { pos -> onBusinessDetailsClick(discounongroceryList[pos].id) }
        rvDiscountGroceries!!.isNestedScrollingEnabled = false
    }

    private fun initTopRestaurant() {
        rvTopRestaurant!!.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val name = HomeFragment::class.java.name
        val homeNewOfferAdapter = HomeNewOfferAdapter(activity, topRestaurantList, "res")
        rvTopRestaurant!!.adapter = homeNewOfferAdapter
        homeNewOfferAdapter.setRestaurantClick { pos -> onBusinessDetailsClick(topRestaurantList[pos].id) }
        rvTopRestaurant!!.isNestedScrollingEnabled = false
    }

    private fun initNearestPubs() {
        rvNearestPubs!!.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val name = HomeFragment::class.java.name
        val homeNewOfferAdapter = HomeNewOfferAdapter(activity, nearestPubList, "pub")
        rvNearestPubs!!.adapter = homeNewOfferAdapter
        homeNewOfferAdapter.setRestaurantClick { pos -> onBusinessDetailsClick(nearestPubList[pos].id) }
        rvNearestPubs!!.isNestedScrollingEnabled = false
    }

    private fun initOffer() {
        rvOfferImage!!.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val name = HomeFragment::class.java.name
        rvOfferImage!!.adapter = HomeOfferAdapter(activity)
        rvOfferImage!!.isNestedScrollingEnabled = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtils.PERMISSION_REQUEST_CODE) {
            permissionUtils.onRequestPermissionsResult()
        }
    }

    companion object {
        const val TAG = "HomeFragment"
        var tvAddress: AppCompatTextView? = null
        @JvmStatic
        fun instance(): HomeFragment {
            return HomeFragment()
        }
    }
}