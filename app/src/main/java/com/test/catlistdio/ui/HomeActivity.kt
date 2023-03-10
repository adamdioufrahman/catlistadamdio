package com.test.catlistdio.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import com.test.catlistdio.R
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.models.entity.UserProfileEntity
import com.test.catlistdio.ui.fragment.CatBreedsListFragment
import com.test.catlistdio.ui.fragment.HomeActivityViewModel

import com.test.catlistdio.models.CatBreedListItem
import com.test.catlistdio.ui.fragment.CatBreedsDetailFragment
import com.test.catlistdio.utils.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*


class HomeActivity : BaseActivity() {
    private var drawerLayout: DrawerLayout? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var viewModel: HomeActivityViewModel
    companion object {
        const val FROM = "com.test.catlistdio.ui.from"
    }
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_home
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()

        handleButtonClicks()
        setupDefaultFragment()
        updateAppVersion()
    }

    /**
     * Inisialisasi model tampilan
     */
    override fun onViewModelInit() {
        viewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeActivityViewModel(applicationContext as SynchronyApp) as T
            }
        }).get(HomeActivityViewModel::class.java)
        initObservers()
        viewModel.getProfile()
    }

    /**
     * Tangani klik tombol edit profil
     * menangani klik ikon bilah navigasi.
     */
    private fun handleButtonClicks() {
        sideMenuIcon.setOnClickListener {
            openNavigationalDrawer()
        }
        labelEditProfile.setOnClickListener {
            launchEditProfile()
        }
    }
    /**
     * Buka buat aktivitas profil dalam mode edit.
     */
    private fun launchEditProfile() {
        closeNavigationDrawer()
        val i = Intent(this@HomeActivity, CreateProfileActivity::class.java)
        i.putExtra(FROM,true)
        launchCreateProfileActivity.launch(i)
    }

    /**
     * Baca versi aplikasi dan setel ke tampilan teks
     */
    private fun updateAppVersion() {
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            val appVersion = pInfo.versionName
            appVersionTv.text = getString(R.string.app_version, appVersion)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }
    }

    /**
     * CreateProfileActivity Launcher untuk Edit data profil.
     */
    var launchCreateProfileActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getProfile()
        }
    }

    /**
     * Tetapkan fragmen daftar sebagai default
     */
    private fun setupDefaultFragment() {
        val fragment: Fragment = CatBreedsListFragment.newInstance()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment, "list_fragment")
        transaction.commit()
    }

    private fun closeNavigationDrawer() {
        drawerLayout?.closeDrawer(GravityCompat.START, true)
    }
    private fun openNavigationalDrawer() {
      drawerLayout?.openDrawer(GravityCompat.START, true)
    }

    /**
     * Perbarui UI dengan data profil
     */
    private fun initObservers() {
        viewModel.getProfileObserver().observe(this, Observer <List<UserProfileEntity>>{
            val userProfileEntity = it[0]
            labelName.text = String.format(getString(R.string.nav_name), userProfileEntity.fName, userProfileEntity.lName)
            labelEmail.text = String.format(getString(R.string.nav_email),userProfileEntity.email)
            labelPhone.text = String.format(getString(R.string.nav_phone),userProfileEntity.phone)
            labelAbout.text = String.format(getString(R.string.nav_about),userProfileEntity.about)
            labelEducation.text = String.format(getString(R.string.nav_education),userProfileEntity.education)
            labelWork.text = String.format(getString(R.string.nav_work),userProfileEntity.work)

            if(!TextUtils.isEmpty(userProfileEntity.image)) {
                val encodedString  = userProfileEntity.image
                val bitmap = Utils.getBitmapFromEncodedStr(encodedString)
                findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
            }
        })
    }

    /**
     * Buka fragmen detail kucing.
     */
    fun openDetailFragment(catBreedListItem: CatBreedListItem) {
        val fragment: Fragment = CatBreedsDetailFragment.newInstance(catBreedListItem)
        val transaction: FragmentTransaction = supportFragmentManager!!.beginTransaction()
        transaction.hide(supportFragmentManager?.findFragmentByTag("list_fragment")!!)
        transaction.add(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}