package com.deal.bytee.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.deal.bytee.R
import com.deal.bytee.Utils.App
import com.deal.bytee.Utils.Endpoints
import com.deal.bytee.Utils.MySharedPreferences
import com.deal.bytee.Utils.MyUtils
import com.deal.bytee.adapter.AddCartVoucherAdapter
import com.deal.bytee.model.CartAddVoucherModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PaymentActivity : BaseActivity(), PaymentResultListener {
    private var cvBack: CardView? = null
    private var toolbarTitle: AppCompatTextView? = null
    private var btnPay: AppCompatButton? = null
    private var VoucherPriceFinal = ""
    private var TOTALAMOUNT = 0
    private var VoucherIDFinal: String? = ""
    private var BusinessName: String? = ""
    private var Discount: String? = ""
    var BusinessId: String? = ""
    private var swipe: SwipeRefreshLayout? = null
    private var rvVoucehrs: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        App.sharedPreferences.setKey(MySharedPreferences.isFromDetails, MySharedPreferences.NO)
        initView()
        initData()
        bindClick()
    }

    var alreadyadded = false
    private fun initData() {
        val intent = intent
        if (intent != null) {
            BusinessName = intent.getStringExtra("businessname")
            Discount = intent.getStringExtra("discount")
            // VoucherPriceFinal = intent.getStringExtra("VoucherPriceFinal");
            VoucherPriceFinal = "1"
            VoucherIDFinal = intent.getStringExtra("VoucherIDFinal")
            BusinessId = intent.getStringExtra("BusinessId")
            val model = CartAddVoucherModel()
            model.voucherid = VoucherIDFinal
            model.businessid = BusinessId
            model.businessname = BusinessName
            model.discount = Discount
            model.amount = VoucherPriceFinal.toInt()
            for (i in MyUtils.cartAddVoucherModelArrayList.indices) {
                if (BusinessId == MyUtils.cartAddVoucherModelArrayList[i].businessid) {
                    alreadyadded = true
                }
            }
            if (!alreadyadded) {
                MyUtils.cartAddVoucherModelArrayList.add(model)
            }
            setData()
        }
    }

    var cartVoucherAdapter: AddCartVoucherAdapter? = null
    private fun setData() {
        for (i in MyUtils.cartAddVoucherModelArrayList.indices) {
            TOTALAMOUNT += MyUtils.cartAddVoucherModelArrayList[i].amount
        }
        btnPay!!.text = "Proceed to pay -> Rs.$TOTALAMOUNT"
        rvVoucehrs!!.layoutManager =
            LinearLayoutManager(this@PaymentActivity, LinearLayoutManager.VERTICAL, false)
        cartVoucherAdapter =
            AddCartVoucherAdapter(this@PaymentActivity, MyUtils.cartAddVoucherModelArrayList)
        cartVoucherAdapter!!.setRemoveVoucherClick { pos -> askForDelete(pos) }
        rvVoucehrs!!.adapter = cartVoucherAdapter
        rvVoucehrs!!.isNestedScrollingEnabled = false
    }

    private fun askForDelete(id: Int) {
        SweetAlertDialog(this@PaymentActivity, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Remove")
            .setContentText("Are you sure want to Remove?")
            .setCancelText("Cancel")
            .setConfirmText("Yes")
            .setCancelClickListener { sDialog -> sDialog.cancel() }
            .setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.cancel()
                MyUtils.cartAddVoucherModelArrayList.removeAt(id)
                cartVoucherAdapter!!.notifyDataSetChanged()
            }
            .show()
    }

    private fun bindClick() {
        cvBack!!.setOnClickListener { handleBackStack() }
        btnPay!!.setOnClickListener {
            SweetAlertDialog(this@PaymentActivity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Buy")
                .setContentText("Are you sure want to Buy Voucher?")
                .setCancelText("Cancel")
                .setConfirmText("Yes")
                .setCancelClickListener { sDialog -> sDialog.cancel() }
                .setConfirmClickListener { sweetAlertDialog ->
                    sweetAlertDialog.cancel()
                    //todo api call for buy
                    if (VoucherIDFinal!!.isEmpty() && VoucherIDFinal === "") {
                        MyUtils.showTheToastMessage("Please select voucher")
                    } else {
                        Log.d("voucherid>>>>", VoucherIDFinal!!)
                        val order_id = "order_rcptid_" + MyUtils.generateRandomeNumber()
                        callapigenerateorderid(order_id)
                    }
                }
                .show()

            // startPayment(order_id,"1");
        }
    }

    private fun callapigenerateorderid(order_id: String) {
        MyUtils.showProgressDialog(this@PaymentActivity, false)
        var islogin = ""
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
        }
        Log.d("price>>>>>", VoucherPriceFinal)
        val js = JSONObject()
        try {
            if (islogin == MySharedPreferences.YES) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id))
                js.put("order_rcptid", order_id)
                js.put("amount", VoucherPriceFinal)
                // js.put("amount", "1");
            } else {
                js.put("userid", "")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request = JsonObjectRequest(
            Request.Method.POST, Endpoints.ORDER_ID_GENERATE_API, js,
            { obj ->
                Log.d("ORDER_ID_>>>>>", obj.toString())
                MyUtils.dismisProgressDialog()
                try {
                    if (obj.getInt("status") == 1) {
                        //  MyUtils.showTheToastMessage(obj.getString("message"));
                        val orderdetails = obj.getString("orderdetails")
                        VoucherPriceFinal = obj.getString("amount")
                        startPayment(orderdetails, VoucherPriceFinal)
                    } else {
                        MyUtils.showTheToastMessage(obj.getString("message"))
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

    fun startPayment(orderId: String?, payAmount: String?) {
        val checkout = Checkout()
        checkout.setKeyID(MySharedPreferences.RAZOR_PAY_KEY_VALUE)
        checkout.setImage(R.drawable.logo)
        try {
            val options = JSONObject()
            options.put("name", App.sharedPreferences.getKey(MySharedPreferences.name))
            options.put("order_id", orderId)
            options.put("currency", "INR")
            options.put("amount", payAmount)
            val preFill = JSONObject()
            preFill.put("email", App.sharedPreferences.getKey(MySharedPreferences.email))
            preFill.put("contact", App.sharedPreferences.getKey(MySharedPreferences.mobile))
            options.put("prefill", preFill)
            checkout.open(this@PaymentActivity, options)
        } catch (e: Exception) {
            Log.d("TAG>>>>>", "Error in starting Razorpay Checkout", e)
        }
    }

    private fun initView() {
        cvBack = findViewById<View>(R.id.cvBack) as CardView
        toolbarTitle = findViewById<View>(R.id.toolbarTitle) as AppCompatTextView
        btnPay = findViewById<View>(R.id.btnPay) as AppCompatButton
        swipe = findViewById<View>(R.id.swipe) as SwipeRefreshLayout
        rvVoucehrs = findViewById<View>(R.id.rvVoucehrs) as RecyclerView
    }

    override fun onPaymentSuccess(razorpayPaymentID: String) {
        Log.d("TAG>>>>>", "onPaymentSuccess  $razorpayPaymentID")
        // apicallForOrderPlace(razorpayPaymentID);
        apicallForBuyVoucher(razorpayPaymentID)
    }

    override fun onPaymentError(i: Int, s: String) {
        Log.d("TAG>>>>>", "onPaymentError  $s")
        try {
            val obj = JSONObject(s)
            val error = obj.getJSONObject("error")
            val description = error.getString("description")
            MyUtils.showTheToastMessage(description)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun apicallForBuyVoucher(razorpayPaymentID: String) {
        MyUtils.showProgressDialog(this@PaymentActivity, false)
        var islogin = ""
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin)
        }
        val js = JSONObject()
        try {
            if (islogin == MySharedPreferences.YES) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id))
                val vouchers = JSONArray()
                for (i in MyUtils.cartAddVoucherModelArrayList.indices) {
                    val josub = JSONObject()
                    josub.put("v_id", MyUtils.cartAddVoucherModelArrayList[i].voucherid)
                    josub.put("b_id", MyUtils.cartAddVoucherModelArrayList[i].businessid)
                    vouchers.put(josub)
                }
                js.put("vouchers", vouchers)
                js.put("totalamount", TOTALAMOUNT)
                js.put("transactionid", razorpayPaymentID)
            } else {
                js.put("userid", "")
                js.put("businessid", BusinessId)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request = JsonObjectRequest(
            Request.Method.POST, Endpoints.BUY_VOUCHER, js,
            { obj ->
                Log.d("buyvouchers>>>", obj.toString())
                MyUtils.dismisProgressDialog()
                try {
                    if (obj.getInt("status") == 1) {
                        MyUtils.cartAddVoucherModelArrayList.clear()
                        //MyUtils.showTheToastMessage(obj.getString("message"));
                        SweetAlertDialog(this@PaymentActivity, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Done!")
                            .setContentText(
                                """
    Thank You For Buying Voucher!
    ${obj.getString("message")}
    """.trimIndent()
                            )
                            .setConfirmText("OKAY")
                            .setConfirmClickListener { sweetAlertDialog ->
                                sweetAlertDialog.cancel()
                                handleBackStack()
                                DashBoardActivity.viewPager!!.currentItem = 2
                                DashBoardActivity.bottomNavigation!!.selectedItemId = R.id.nav_cart
                                DashBoardActivity.tabAdapter!!.notifyDataSetChanged()
                                //  String name = RestaurantDetailsFragment.class.getName();
                                //tabAdapter.getItemPosition(name);
                            }
                            .show()
                    } else {
                        MyUtils.showTheToastMessage(obj.getString("message"))
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
}