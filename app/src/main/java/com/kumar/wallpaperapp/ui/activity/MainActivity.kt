package com.kumar.wallpaperapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kumar.wallpaperapp.R
import com.kumar.wallpaperapp.databinding.ActivityMainBinding
import com.kumar.wallpaperapp.network.NetworkConnectionInterceptor
import com.kumar.wallpaperapp.network.RetrofitInstance
import com.kumar.wallpaperapp.respository.Repository
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModel
import com.kumar.wallpaperapp.ui.viewmodel.WallpaperViewModelFactory
import com.kumar.wallpaperapp.utils.ToastListener
import com.kumar.wallpaperapp.utils.toast

class MainActivity : AppCompatActivity() , ToastListener {
    private var binding: ActivityMainBinding? = null
    lateinit var viewModel: WallpaperViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val networkConnectionInterceptor = NetworkConnectionInterceptor(this)

        viewModel = ViewModelProvider(
            this,
            WallpaperViewModelFactory(Repository(RetrofitInstance.invoke(networkConnectionInterceptor)))
        ).get(WallpaperViewModel::class.java)

        setUpNavController()
    }

    fun setUpNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val navController = navHostFragment?.findNavController()
        binding?.bottomNavigationView?.setupWithNavController(navController!!)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onFailure(message: String) {
        this.toast(message)
    }
}