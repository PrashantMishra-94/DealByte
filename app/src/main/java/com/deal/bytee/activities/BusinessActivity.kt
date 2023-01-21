package com.deal.bytee.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.deal.bytee.R
import com.deal.bytee.fragment.BusinessDetailsFragment
import com.deal.bytee.fragment.BusinessDetailsFragment.Companion.BUSINESS_ID

class BusinessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)
        changeStatusBarColor(ContextCompat.getColor(this, R.color.main))
        val businessId = intent.getStringExtra(BUSINESS_ID)!!
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        /*fragmentTransaction.setCustomAnimations(
            R.anim.fragment_open_enter,
            R.anim.fragment_close_exit
        )*/
        val tag = BusinessDetailsFragment::class.java.name
        fragmentTransaction.add(R.id.container, BusinessDetailsFragment.getInstance(businessId), tag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    companion object {
        fun start(activity: Activity, businessId: String) {
            val intent = Intent(activity, BusinessActivity::class.java)
            intent.putExtra(BUSINESS_ID, businessId)
            activity.startActivity(intent)
        }
    }
}