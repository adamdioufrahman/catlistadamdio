package com.test.catlistdio.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.catlistdio.R
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.models.entity.UserProfileEntity
import com.test.catlistdio.ui.fragment.HomeActivityViewModel
import com.test.catlistdio.viewmodel.repository.SplashViewModel

class SplashActivity : BaseActivity() {
    private lateinit var viewModel: SplashViewModel
    private lateinit var  homeActivityViewModel: HomeActivityViewModel
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_splash
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        initObservers()
    }

    /**
     * Inisialisasi model tampilan
     */
    override fun onViewModelInit() {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        homeActivityViewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeActivityViewModel(applicationContext as SynchronyApp) as T
            }
        }).get(HomeActivityViewModel::class.java)
    }

    /**
     * Daftarkan pengamat untuk mengamati hasil dari model tampilan
     */
    private fun initObservers() {
        viewModel.getSplashObserver().observe(this, Observer <Boolean>{
            homeActivityViewModel.getProfile()
        })
        homeActivityViewModel.getProfileObserver().observe(this, Observer <List<UserProfileEntity>>{
            if(it != null && it.size > 0)
                launchHome()
            else
                launchCreateProfile()
        })
        viewModel.startSplashDelay()
    }

    /**
     * Luncurkan aktivitas daftar sekolah dan tutup sendiri
     */
    private fun launchCreateProfile() {
        val i = Intent(this@SplashActivity, CreateProfileActivity::class.java)
        startActivity(i)
        finish()
    }

    /**
     * Luncurkan aktivitas RUMAH.
     */
    private fun launchHome() {
        val i = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(i)
        finish()
    }
}