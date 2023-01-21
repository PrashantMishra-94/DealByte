package com.deal.bytee.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.deal.bytee.R

class PackageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package)
        val url = intent.getStringExtra(PACKAGE_URL)
        val imageView = findViewById<ImageView>(R.id.imgPackage)
        url?.let {
            Glide.with(this).load(url)
                .placeholder(R.drawable.logoo)
                .into(imageView)
        } ?: kotlin.run {
            Toast.makeText(this, "Package not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    companion object {
        const val PACKAGE_URL = "PACKAGE_URL"
    }
}