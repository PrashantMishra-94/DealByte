package com.deal.bytee.fragment

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.deal.bytee.R
import com.deal.bytee.Utils.*
import com.deal.bytee.activities.*
import com.deal.bytee.adapter.BusinessFacilityAdapter
import com.deal.bytee.adapter.BusinessGalleryAdapter
import com.deal.bytee.adapter.BusinessMenuAdapter
import com.deal.bytee.adapter.BusinessReviewAdapter
import com.deal.bytee.data.model.BusinessDetails
import com.deal.bytee.data.model.DetailedData
import com.deal.bytee.data.model.OpenTime
import com.deal.bytee.data.model.Voucher
import com.deal.bytee.databinding.FragmentRestaurantDetailsBinding
import com.deal.bytee.viewModel.BusinessDetailsViewModel
import com.deal.bytee.viewModel.ViewModelFactory
import com.willy.ratingbar.ScaleRatingBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BusinessDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentRestaurantDetailsBinding
    private var businessId = ""
    private var userId = ""
    private lateinit var viewModel: BusinessDetailsViewModel
    private var liveBusinessDetails: LiveData<Resource<BusinessDetails>>? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var businessDetails: BusinessDetails? = null
    private var detailedBusinessDetails: DetailedData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        businessId = arguments?.getString(BUSINESS_ID) ?: ""
        userId = App.sharedPreferences.getKey(MySharedPreferences.id) ?: ""
        viewModel = ViewModelFactory.get(this, BusinessDetailsViewModel::class.java)
        refreshBusinessDetails()
        binding.apply {
            llCatering.visibility = View.GONE
            llVoucher.visibility = View.GONE
            lytPackageContainer.visibility = View.GONE
            swipe.setOnRefreshListener {
                swipe.isRefreshing = false
                refreshBusinessDetails()
            }

            llBack.setOnClickListener { requireActivity().finish() }

            btnCall.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+917795176850"))
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        101
                    )
                } else {
                    startActivity(intent)
                }
            }

            ivRate.setOnClickListener {
                if (isLogin()) {
                    showDialogToAskForRateReview()
                } else {
                    startLoginActivity()
                }
            }

            ivLike.setOnClickListener {
                if (isLogin()) {
                    addFavourite()
                } else {
                    startLoginActivity()
                }
            }

            tvBuyVoucher.setOnClickListener {
                if (isLogin()) {
                    if (businessDetails?.voucherBoughtStatus != 0) {
                        SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Purchased")
                            .setContentText(businessDetails?.voucherBuyStatusMessage ?: "")
                            .setConfirmText("OKAY")
                            .setConfirmClickListener { sweetAlertDialog -> sweetAlertDialog.cancel() }
                            .show()
                        return@setOnClickListener
                    }
                    if (businessDetails?.vouchers.isNullOrEmpty()) {
                        MyUtils.showTheToastMessage("No voucher available")
                        return@setOnClickListener
                    }
                    val voucher = binding.spSelectVoucher.selectedItem as? Voucher
                    if (voucher?.vId.isNullOrEmpty()) {
                        MyUtils.showTheToastMessage("Please select voucher")
                        return@setOnClickListener
                    }
                    BuyVoucherActivity.startActivity(
                        requireActivity(),
                        voucher!!,
                        businessId,
                        detailedBusinessDetails?.title ?: ""
                    )
                } else {
                    startLoginActivity()
                }
            }

            btnAskQuote.setOnClickListener { askForQuoteForm() }

            ivLocation.setOnClickListener { v: View? ->
                if (latitude > 0 && longitude > 0) {
                    val intent = Intent(requireActivity(), MapActivity::class.java)
                    intent.putExtra(MapActivity.LATITUDE, latitude)
                    intent.putExtra(MapActivity.LONGITUDE, longitude)
                    intent.putExtra(MapActivity.TITLE, detailedBusinessDetails?.title)
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnGold.setOnClickListener(packageClickListener)
            btnSilver.setOnClickListener(packageClickListener)
            btnBronze.setOnClickListener(packageClickListener)
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.putExtra(LoginActivity.IS_FROM_DETAILS, true)
        intent.putExtra(LoginActivity.BUSINESS_ID, businessId)
        startActivity(intent)
    }

    private fun refreshBusinessDetails() {
        liveBusinessDetails?.removeObservers(viewLifecycleOwner)
        liveBusinessDetails = viewModel.refreshBusinessDetails(userId, businessId)
        liveBusinessDetails!!.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    MyUtils.showProgressDialog(context, false)
                }
                Status.ERROR -> {
                    MyUtils.dismisProgressDialog()
                    MyUtils.showTheToastMessage(it.message)
                }
                Status.SUCCESS -> {
                    MyUtils.dismisProgressDialog()
                    businessDetails = it.data
                    if (businessDetails?.status != 1) {
                        MyUtils.showTheToastMessage(businessDetails?.message)
                        return@observe
                    }
                    if (!it.data?.detailedData.isNullOrEmpty()) {
                        detailedBusinessDetails = it.data!!.detailedData!![0]
                    }
                    detailedBusinessDetails?.let {  detailedData ->
                        latitude = detailedData.lat?.toDoubleOrNull() ?: 0.0
                        longitude = detailedData.long?.toDoubleOrNull() ?: 0.0
                        setBusinessDetails(detailedData)
                        if (!detailedData.openTime.isNullOrEmpty()) {
                            setOpenTime(detailedData.openTime!![0])
                        }
                    }
                    businessDetails?.morepics?.let { pics ->
                        binding.apply {
                            rvRestaurantGallery.layoutManager = LinearLayoutManager(requireActivity(),
                                LinearLayoutManager.HORIZONTAL, false)
                            rvRestaurantGallery.isNestedScrollingEnabled = false
                            rvRestaurantGallery.adapter = BusinessGalleryAdapter(pics)
                        }
                    }
                    if (!businessDetails?.facilities.isNullOrEmpty()) {
                        binding.rvFacilities.apply {
                            layoutManager = LinearLayoutManager(requireActivity(),
                                LinearLayoutManager.HORIZONTAL, false)
                            isNestedScrollingEnabled = false
                            adapter = BusinessFacilityAdapter(businessDetails!!.facilities!!)
                        }
                    }
                    if (!businessDetails?.menu.isNullOrEmpty()) {
                        val menu = businessDetails!!.menu!![0]
                        if (!menu?.subCategory.isNullOrEmpty()) {
                            binding.rvMenu.apply {
                                layoutManager = LinearLayoutManager(requireActivity(),
                                    LinearLayoutManager.HORIZONTAL, false)
                                isNestedScrollingEnabled = false
                                adapter = BusinessMenuAdapter(menu!!.subCategory!!)
                            }
                        }
                    }
                    if (!businessDetails?.reviews.isNullOrEmpty()) {
                        binding.rvReview.apply {
                            layoutManager = LinearLayoutManager(requireActivity(),
                                LinearLayoutManager.HORIZONTAL, false)
                            isNestedScrollingEnabled = false
                            adapter = BusinessReviewAdapter(businessDetails!!.reviews!!)
                        }
                    }
                    if (!businessDetails?.vouchers.isNullOrEmpty()) {
                        binding.spSelectVoucher.adapter = ArrayAdapter(requireContext(),
                            R.layout.lyt_spinner_text, businessDetails!!.vouchers!!)
                    } else {
                        binding.spSelectVoucher.adapter = ArrayAdapter(requireContext(),
                            R.layout.lyt_spinner_text, arrayListOf("Select Voucher"))
                    }
                }
            }
        }
    }

    private fun setBusinessDetails(detailedData: DetailedData) {
        binding.apply {
            if (detailedBusinessDetails?.mainCategory == "Catering") {
                llCatering.visibility = View.VISIBLE
                llVoucher.visibility = View.GONE
                lytPackageContainer.visibility = View.VISIBLE
            } else {
                llCatering.visibility = View.GONE
                llVoucher.visibility = View.VISIBLE
                lytPackageContainer.visibility = View.GONE
            }
            tvRestaurantName.text = detailedBusinessDetails?.title
            detailedData.discount?.let { discount ->
                tvDiscounttxt.text = "$discount% Discount"
            }
            tvLocation.text = "${detailedData.area}, ${detailedData.city}"
            simpleRatingBar.rating = detailedData.rate?.toFloatOrNull() ?: 0f
            tvReviewtxt.text = "( ${detailedData.reviewCount} Reviews )"
            tvDeliveryType.text = detailedData.deliveryType
            tvRestaurantDetails.text = detailedData.description
        }
    }

    private fun setOpenTime(openTime: OpenTime?) {
        if (openTime == null) return
        binding.apply {
            tvMondayTime.text = openTime.monday
            tvTuesdayTime.text = openTime.tuesday
            tvWednesdayTime.text = openTime.wednesday
            tvThursdayTime.text = openTime.thursday
            tvFridayTime.text = openTime.friday
            tvSaturdayTime.text = openTime.saturday
            tvSundayTime.text = openTime.sunday
        }
    }

    private fun showDialogToAskForRateReview() {
        val dialog = Dialog(requireActivity(), R.style.ChatDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.lyt_rate_review)
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val ratingBar = dialog.findViewById<ScaleRatingBar>(R.id.simpleRatingBar)
        val edtReview = dialog.findViewById<AppCompatEditText>(R.id.edtReview)
        val btnSubmit = dialog.findViewById<AppCompatButton>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val review = edtReview.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(review)) {
                edtReview.requestFocus()
                edtReview.error = "Please enter review"
            } else {
                if (MyUtils.isNetworkAvailable()) {
                    dialog.dismiss()
                    MyUtils.hideKeyboard(activity)
                    addRating(ratingBar.rating, review)
                } else {
                    dialog.dismiss()
                    MyUtils.showTheToastMessage("Please Check Internet Connection!")
                }
            }
        }
        window.attributes = lp
        dialog.show()
    }

    private fun askForQuoteForm() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Ask For Quote")
        val context = requireContext()
        val layout = LinearLayout(context)
        layout.setPadding(20, 20, 20, 20)
        layout.orientation = LinearLayout.VERTICAL
        val namee = EditText(context)
        namee.hint = "Enter Name"
        layout.addView(namee) // Notice this is an add method
        val contact = EditText(context)
        contact.hint = "Enter Contact Number"
        contact.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(contact) // Another add method
        val comment = EditText(context)
        comment.hint = "Enter Comment"
        comment.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        layout.addView(comment)
        builder.setView(layout)
        builder.setPositiveButton(
            "Submit"
        ) { _, _ ->
            val nameitem = namee.text.toString().trim { it <= ' ' }
            val contactt = contact.text.toString().trim { it <= ' ' }
            val commentt = comment.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(nameitem)) {
                namee.requestFocus()
                namee.error = "Please enter name"
            } else if (!MyUtils.isValidMobile(contactt)) {
                contact.requestFocus()
                contact.error = "Please enter contact"
            } else if (TextUtils.isEmpty(commentt)) {
                comment.requestFocus()
                comment.error = "Please enter comment"
            } else {
                if (MyUtils.isNetworkAvailable()) {
                    MyUtils.hideKeyboard(activity)
                    addQuote(nameitem, contactt, commentt)
                } else {
                    MyUtils.showTheToastMessage("Please Check Internet Connection!")
                }
            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun addRating(rating: Float, review: String) {
        viewModel.addReview(userId, businessId, rating, review).observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.LOADING -> {
                    MyUtils.showProgressDialog(requireContext(), false)
                }
                Status.ERROR -> {
                    MyUtils.dismisProgressDialog()
                    MyUtils.showTheToastMessage(response.message)
                }
                Status.SUCCESS -> {
                    MyUtils.dismisProgressDialog()
                    response.data?.let {
                        if (it.status == 1) {
                            SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Done!")
                                .setContentText("Thank You For Your Rate & Review!\n")
                                .setConfirmText("OKAY")
                                .setConfirmClickListener { sweetAlertDialog ->
                                    sweetAlertDialog.cancel()
                                    refreshBusinessDetails()
                                }
                                .show()
                        } else {
                            MyUtils.showTheToastMessage(it.message)
                        }
                    }
                }
            }
        }
    }

    private fun addFavourite() {
        viewModel.addToWishList(userId, businessId).observe(viewLifecycleOwner){ response ->
            when (response.status) {
                Status.LOADING -> {
                    MyUtils.showProgressDialog(requireContext(), false)
                }
                Status.ERROR -> {
                    MyUtils.dismisProgressDialog()
                    MyUtils.showTheToastMessage(response.message)
                }
                Status.SUCCESS -> {
                    MyUtils.dismisProgressDialog()
                    response.data?.let {
                        MyUtils.showTheToastMessage(it.message)
                        if (it.status == 1) {
                            requireActivity().finish()
                            GlobalScope.launch(Dispatchers.Main) {
                                delay(100)
                                DashBoardActivity.viewPager!!.currentItem = 3
                                DashBoardActivity.bottomNavigation!!.selectedItemId = R.id.nav_favourite
                                DashBoardActivity.tabAdapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addQuote(name: String, contact: String, comment: String) {
        // todo
    }

    private val packageClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.btnGold -> openPackage(detailedBusinessDetails?.goldmenu)
                R.id.btnSilver -> openPackage(detailedBusinessDetails?.silvermenu)
                R.id.btnBronze -> openPackage(detailedBusinessDetails?.bronzemenu)
            }
        }

        private fun openPackage(url: String?) {
            if (url.isNullOrBlank()) {
                Toast.makeText(activity, "Package not found", Toast.LENGTH_SHORT).show()
                return
            }
            val intent = Intent(requireActivity(), PackageActivity::class.java)
            intent.putExtra(PackageActivity.PACKAGE_URL, url)
            startActivity(intent)
        }
    }

    companion object {
        const val BUSINESS_ID = "BUSINESS_ID"

        fun getInstance(businessId: String): BusinessDetailsFragment {
            val instance = BusinessDetailsFragment()
            val bundle = Bundle()
            bundle.putString(BUSINESS_ID, businessId)
            instance.arguments = bundle
            return instance
        }
    }

}