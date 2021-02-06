package com.kumar.wallpaperapp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.kumar.wallpaperapp.R
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (hasInternet()) {
            Timer().schedule(object: TimerTask(){
                override fun run() {
                    gotoMainActivity()
                }

            }, 2000)
        }
        else {
            val layout = findViewById<View>(R.id.splashLayout)
            Snackbar.make(layout, "No Internet", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY") {
                    if (hasInternet()) {
                        gotoMainActivity()
                    }
                }.show()
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show()
        }
    }

    fun gotoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun hasInternet() : Boolean {
            var result = false
            val cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return result
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = true
                }
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = true
                }
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    result = true
                }
            } else {
                val networkInfo: NetworkInfo? = cm.activeNetworkInfo
                if (networkInfo != null) {
                    if (networkInfo.type == ConnectivityManager.TYPE_WIFI) result = true
                }
                if (networkInfo != null) {
                    if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) result = true
                }
                if (networkInfo != null) {
                    if (networkInfo.type == ConnectivityManager.TYPE_VPN) result = true
                }

            }
            return result

    }
}