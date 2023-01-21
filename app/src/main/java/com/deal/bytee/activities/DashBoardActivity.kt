package com.deal.bytee.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.deal.bytee.R
import com.deal.bytee.Utils.*
import com.deal.bytee.adapter.TabAdapter
import com.deal.bytee.fragment.*
import com.deal.bytee.viewModel.DashBoardViewModel
import com.deal.bytee.viewModel.ViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.internal.Intrinsics

class DashBoardActivity : BaseActivity() {
    private var bottomLayout: FrameLayout? = null
    private var fab: ImageView? = null
    private var VoucherPriceFinal = ""
    private var VoucherIDFinal: String? = ""
    private var BusinessName: String? = ""
    private var Discount: String? = ""
    var BusinessId: String? = ""
    private lateinit var viewModel: DashBoardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        changeStatusBarColor(ContextCompat.getColor(this, R.color.main))
        viewModel = ViewModelFactory.get(this, DashBoardViewModel::class.java)
        initView()
        checkFromDetails()
        checkForCart(intent)
        var islogin = ""
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
        }
        if (islogin == MySharedPreferences.YES) {
            //apicallfetchdata()
            getProfile()
        } else {
            initTabs()
        }
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                App.sharedPreferences.setKey(MySharedPreferences.NO, token)
                println("FCM Token-----------$token")
                var islogin = ""
                if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
                    islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
                }
                if (islogin == MySharedPreferences.YES) {
                    updatetokeapi(token)
                }
            })
    }

    private fun checkFromDetails() {
        /*var isFromDetails = ""
        if (App.sharedPreferences.chk(MySharedPreferences.isFromDetails)) {
            isFromDetails = App.sharedPreferences.getKey(MySharedPreferences.isFromDetails)
        }
        if (isFromDetails == MySharedPreferences.YES) {
            initData()
        }*/
        if (intent.getBooleanExtra(LoginActivity.IS_FROM_DETAILS, false)) {
            intent.getStringExtra(LoginActivity.BUSINESS_ID)?.let { businessId ->
                BusinessActivity.start(this, businessId)
            }

        }
    }

    /*private fun initData() {
        val intent = intent
        if (intent != null) {
            BusinessName = intent.getStringExtra("businessname")
            Discount = intent.getStringExtra("discount")
            // VoucherPriceFinal = intent.getStringExtra("VoucherPriceFinal");
            VoucherPriceFinal = "1"
            VoucherIDFinal = intent.getStringExtra("VoucherIDFinal")
            BusinessId = intent.getStringExtra("BusinessId")
            val mintent = Intent(this@DashBoardActivity, PaymentActivity::class.java)
            mintent.putExtra("businessname", BusinessName)
            mintent.putExtra("discount", Discount)
            mintent.putExtra("VoucherPriceFinal", VoucherPriceFinal)
            mintent.putExtra("VoucherIDFinal", VoucherIDFinal)
            mintent.putExtra("BusinessId", BusinessId)
            startActivity(mintent)
        }
    }*/

    private fun updatetokeapi(token: String) {

        // MyUtils.showProgressDialog(DashBoardActivity.this, false);
        var islogin = ""
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
        }
        val js = JSONObject()
        try {
            if (islogin == MySharedPreferences.YES) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id))
                js.put("token", token)
            } else {
                js.put("userid", "")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request = JsonObjectRequest(
            Request.Method.POST, Endpoints.UPDATE_TOKEN, js,
            { obj ->
                Log.d("token>>>>", obj.toString())
                // MyUtils.dismisProgressDialog();
                try {
                    if (obj.getInt("status") == 1) {
                        //  MyUtils.showTheToastMessage(obj.getString("message"));
                    } else {
                        // MyUtils.showTheToastMessage(obj.getString("message"));
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
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

    private fun getProfile() {
        viewModel.getProfileDetails(App.sharedPreferences.getKey(MySharedPreferences.id))
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> { /*do Nothing*/ }
                    Status.SUCCESS -> {
                        Log.d(TAG, "getProfile: ${it.data}")
                        if (it.data?.status == 1) {
                            val data = it.data
                            App.sharedPreferences.setKey(
                                MySharedPreferences.id,
                                data.userid
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.name,
                                data.name
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.mobile,
                                data.mobile
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.email,
                                data.email
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.pass,
                                data.password
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.address,
                                data.address
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.profile_pic,
                                data.profile_pic
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.aadhar_number,
                                data.aadhar_number
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.aadhar_front,
                                data.aadhar_front
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.aadhar_back,
                                data.aadhar_back
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.pan_number,
                                data.pan_number
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.pan_front,
                                data.pan_front
                            )
                            App.sharedPreferences.setKey(MySharedPreferences.lat, data.lat)
                            App.sharedPreferences.setKey(
                                MySharedPreferences.longi,
                                data.long
                            )
                            App.sharedPreferences.setKey(
                                MySharedPreferences.kyc_verify,
                                data.kyc_verify
                            )
                            initTabs()
                        } else {
                            Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    fun apicallfetchdata() {
        // MyUtils.showProgressDialog(DashBoardActivity.this, false);
        val js = JSONObject()
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request = JsonObjectRequest(
            Request.Method.POST, Endpoints.FETCH_PROFILE_API, js,
            { obj ->
                Log.d("userdataa>>>>", obj.toString())
                //    MyUtils.dismisProgressDialog();
                try {
                    if (obj.getInt("status") == 1) {
                        //  MyUtils.showTheToastMessage(obj.getString("message"));
                        App.sharedPreferences.setKey(
                            MySharedPreferences.id,
                            obj.getString("userid")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.name,
                            obj.getString("name")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.cashback_earned,
                            obj.getString("cashback_earned")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.mobile,
                            obj.getString("mobile")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.email,
                            obj.getString("email")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.pass,
                            obj.getString("password")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.address,
                            obj.getString("address")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.profile_pic,
                            obj.getString("profile_pic")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.aadhar_number,
                            obj.getString("aadhar_number")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.aadhar_front,
                            obj.getString("aadhar_front")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.aadhar_back,
                            obj.getString("aadhar_back")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.pan_number,
                            obj.getString("pan_number")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.pan_front,
                            obj.getString("pan_front")
                        )
                        App.sharedPreferences.setKey(MySharedPreferences.lat, obj.getString("lat"))
                        App.sharedPreferences.setKey(
                            MySharedPreferences.longi,
                            obj.getString("long")
                        )
                        App.sharedPreferences.setKey(
                            MySharedPreferences.kyc_verify,
                            obj.getString("kyc_verify")
                        )
                        initTabs()
                    } else {
                        //  MyUtils.showTheToastMessage(obj.getString("message"));
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
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

    private fun initTabs() {
        val fragmentManager = this.supportFragmentManager
        Intrinsics.checkExpressionValueIsNotNull(fragmentManager as Any, "supportFragmentManager")

        tabAdapter = TabAdapter(fragmentManager)
        tabAdapter!!.addFragment(TabHomeFragment(), "Home")
        tabAdapter!!.addFragment(TabProfileFragment(), "Profile")
        tabAdapter!!.addFragment(TabCartFragment(), "Cart")
        tabAdapter!!.addFragment(TabFavouriteFragment(), "Favourite")
        //tabAdapter!!.addFragment(TabDiscoverFragment(), "Discover")
        tabAdapter!!.addFragment(SupportFragment(), "Support")
        Intrinsics.checkExpressionValueIsNotNull(viewPager, "viewPager")
        viewPager!!.offscreenPageLimit = tabAdapter!!.count
        Intrinsics.checkExpressionValueIsNotNull(viewPager, "viewPager")
        viewPager!!.adapter = tabAdapter
        bottomNavigation!!.setOnNavigationItemSelectedListener { menuItem ->
            Intrinsics.checkParameterIsNotNull(menuItem as Any, "item")
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Intrinsics.checkExpressionValueIsNotNull(viewPager, "viewPager")
                    viewPager!!.currentItem = 0
                    true
                }
                /*R.id.nav_discover -> {
                    Intrinsics.checkExpressionValueIsNotNull(viewPager, "viewPager")
                    viewPager!!.currentItem = 1
                    true
                }*/
                R.id.nav_profile -> {
                    var islogin = ""
                    if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
                        islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
                    }
                    if (islogin != MySharedPreferences.YES) {
                        startActivity(Intent(this@DashBoardActivity, LoginActivity::class.java))
                        //DashBoardActivity.this.finish();
                    } else {
                        Intrinsics.checkExpressionValueIsNotNull(viewPager, "viewPager")
                        viewPager!!.currentItem = 1
                    }
                    true
                }
                R.id.nav_cart -> {

                    //TODO check for login
                    Intrinsics.checkExpressionValueIsNotNull(viewPager, "viewPager")
                    viewPager!!.currentItem = 2
                    true
                }
                R.id.nav_favourite -> {
                    Intrinsics.checkExpressionValueIsNotNull(viewPager, "viewPager")
                    viewPager!!.currentItem = 3
                    true
                }
                R.id.nav_support -> {
                    Intrinsics.checkExpressionValueIsNotNull(viewPager, "viewPager")
                    viewPager!!.currentItem = 4
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun initView() {
        bottomLayout = findViewById<View>(R.id.bottomLayout) as FrameLayout
        bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        viewPager = findViewById<View>(R.id.viewPager) as NonSwipeableViewPager
        fab = findViewById<View>(R.id.fab) as ImageView
    }

    private fun checkForCart(intent: Intent?) {
        if (intent?.getBooleanExtra(OPEN_CART, false) == true) {
            Log.d(TAG, "onNewIntent: OPEN_CART")
            lifecycleScope.launchWhenResumed {
                delay(500)
                viewPager!!.currentItem = 2
                bottomNavigation!!.selectedItemId = R.id.nav_cart
                tabAdapter!!.notifyDataSetChanged()
            }

        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: ")
        checkForCart(intent)
    }

    override fun onBackPressed() {
        if (viewPager != null && viewPager!!.currentItem != 0) {
            viewPager!!.currentItem = 0
            bottomNavigation!!.selectedItemId = R.id.nav_home
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        const val TAG = "DashBoardActivity"
        const val OPEN_CART = "OPEN_CART"

        @JvmField
        var bottomNavigation: BottomNavigationView? = null

        @JvmField
        var viewPager: NonSwipeableViewPager? = null

        @JvmField
        var tabAdapter: TabAdapter? = null
    }
}