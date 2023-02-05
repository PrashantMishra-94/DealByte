package com.deal.bytee.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import com.deal.bytee.R

class SupportFragment : BaseFragment(R.layout.fragment_support) {

    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_support, container, false)
        view.findViewById<Button>(R.id.btnCall).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+918068380699")
            startActivity(intent)
        }
        view.findViewById<Button>(R.id.btnPrivacyPolicy).setOnClickListener {
            val url = "https://www.dealbyte.in/privacy.html"
            val customTabIntent = CustomTabsIntent.Builder().build()
            customTabIntent.launchUrl(requireContext(), Uri.parse(url))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //showDialog()
        Log.d(TAG, "onViewCreated: ")
    }

    override fun onStart() {
        super.onStart()
        //showDialog()
        Log.d(TAG, "onStart: ")
    }

    override fun onStop() {
        super.onStop()
        //dismissDialog()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        //dismissDialog()
        super.onDestroyView()
    }

    private fun showDialog() {
        if (dialog == null) {
            dialog = AlertDialog.Builder(requireContext())
                .setTitle("Support")
                .setMessage("Call Support")
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:+918068380699")
                    startActivity(intent)
                }.create()
        }
        if (dialog!!.isShowing) {
            dialog!!.show()
        }
        Log.d(TAG, "showDialog: ")
    }

    private fun dismissDialog() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
        Log.d(TAG, "dismissDialog: ")
    }

    companion object {
        const val TAG = "SupportFragment"
    }
}