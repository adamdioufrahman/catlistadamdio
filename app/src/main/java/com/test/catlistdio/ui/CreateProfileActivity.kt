package com.test.catlistdio.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.catlistdio.R
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.models.entity.UserProfileEntity
import com.test.catlistdio.ui.HomeActivity.Companion.FROM
import com.test.catlistdio.utils.DataCallback
import com.test.catlistdio.utils.Utils
import com.test.catlistdio.viewmodel.CreateProfileViewModel
import kotlinx.android.synthetic.main.activity_create_profile.*
import java.io.IOException

class CreateProfileActivity: BaseActivity() {
    private lateinit var viewModel: CreateProfileViewModel
    private var imageData: String? = null
    private var profileId = 0
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_create_profile
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        val forEdit = intent.getBooleanExtra(FROM, false)
        handleSaveButtonClick()
        imageView.setOnClickListener {
            chooseImagePickerClicked()
        }

        cancelButton.setOnClickListener {
            finish()
        }
        if(forEdit) {
            initEditModeObserver()
        }
    }
    /**
     * Inisialisasi model tampilan
     */
    override fun onViewModelInit() {
        viewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CreateProfileViewModel(applicationContext as SynchronyApp) as T
            }
        }).get(CreateProfileViewModel::class.java)
        initObservers()
    }

    /**
     * Daftarkan pengamat untuk mengamati hasil dari model tampilan
     */
    private fun initObservers() {
        viewModel.getCreateProfileObserver().observe(this, Observer <DataCallback<Boolean>>{
            hideProgress()
            when(it?.status) {
                DataCallback.Status.SUCCESS -> {
                    if (profileId == 0) {
                        val i = Intent(this@CreateProfileActivity, HomeActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
                else -> {
                    showError(
                        getString(R.string.common_error_title),
                        getString(R.string.common_error_message),
                        getString(R.string.common_string_ok)
                    )
                }
            }
        })
    }

    /**
     * Inisialisasi mode edit
     * dapatkan data dari DB
     * tampilkan profil di UI.
     */
    private fun initEditModeObserver() {
        viewModel.getProfileObserver().observe(this, Observer <List<UserProfileEntity>>{
            val userProfileEntity = it[0]
            profileId = userProfileEntity.id
            setUIData(userProfileEntity)
            updateButtonText()
            updateScreenTitle()
            if(!TextUtils.isEmpty(userProfileEntity.image)) {
                imageData = userProfileEntity.image
                val bitmap = Utils.getBitmapFromEncodedStr(imageData)
                findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
            }
        })
        viewModel.getProfile()
    }

    /**
     * Perbarui judul layar untuk Edit
     */
    private fun updateScreenTitle() {
        titleLabel.text = getString(R.string.profile_edit_profile_label)
    }

    /**
     * Perbarui tombol simpan untuk memperbarui saat datang untuk mengedit.
     */
    private fun updateButtonText() {
        saveButton.text = getString(R.string.common_string_update)
    }

    /**
     * Perbarui data profil di UI.
     */
    private fun setUIData(userProfileEntity: UserProfileEntity) {
        inputFirstName.setText(userProfileEntity.fName.toString())
        inputLastName.setText(userProfileEntity.lName)
        inputEmail.setText(userProfileEntity.email)
        inputPhone.setText(userProfileEntity.phone)
        inputAbout.setText(userProfileEntity.about)
        inputEducation.setText(userProfileEntity.education)
        inputWork.setText(userProfileEntity.work)
    }

    /**
     * Tangani klik tombol simpan.
     */
    private fun handleSaveButtonClick() {
        saveButton.setOnClickListener {
            validateInputFields()
        }
    }

    /**
     * Buat profil di DB
     */
    private fun createProfile(userProfileEntity: UserProfileEntity) {
        showProgress()
        viewModel.createProfile(userProfileEntity)
    }

    /**
     * Dapatkan data input dan validasi
     * setelah validasi simpan di DB untuk membuat profil.
     */
    private fun validateInputFields() {
        val inputFirstName =  inputFirstName.text.toString()
        val inputLastName =  inputLastName.text.toString()
        val inputEmail =  inputEmail.text.toString()
        val inputPhone =  inputPhone.text.toString()
        val inputAbout =  inputAbout.text.toString()
        val inputEducation =  inputEducation.text.toString()
        val inputWork =  inputWork.text.toString()

        hideAllValidationErrors()
        if(TextUtils.isEmpty(inputFirstName)) {
            errorFirstName.visibility= View.VISIBLE
            return
        } else if(TextUtils.isEmpty(inputLastName)) {
            errorLastName.visibility= View.VISIBLE
            return
        } else if(TextUtils.isEmpty(inputEmail)) {
            errorEmail.visibility= View.VISIBLE
            return
        } else if(TextUtils.isEmpty(inputPhone)) {
            errorPhone.visibility= View.VISIBLE
            return
        } else if(TextUtils.isEmpty(inputEducation)) {
            errorEducation.visibility= View.VISIBLE
            return
        } else if(TextUtils.isEmpty(inputWork)) {
            errorWork.visibility= View.VISIBLE
            return
        } else if(TextUtils.isEmpty(inputAbout)) {
            errorAbout.visibility= View.VISIBLE
            return
        }
        val userProfile = UserProfileEntity(id=profileId, inputFirstName, inputLastName, inputEmail, inputPhone, inputEducation, inputWork, inputAbout , imageData)
        createProfile(userProfile)
    }

    /**
     * Sembunyikan kesalahan yang terlihat saat ini.
     */
    private fun hideAllValidationErrors() {
        errorFirstName.visibility= View.GONE
        errorLastName.visibility= View.GONE
        errorEmail.visibility= View.GONE
        errorPhone.visibility= View.GONE
        errorAbout.visibility= View.GONE
        errorEducation.visibility= View.GONE
        errorWork.visibility= View.GONE
    }

    /**
     * Tampilkan Dialog untuk membuka galeri telepon.
     */
   private fun chooseImagePickerClicked() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.profile_pic_dialog_title))
        builder.setMessage(getString(R.string.profile_pic_dialog_message))
        builder.setPositiveButton(getString(R.string.profile_pic_dialog_button)) { dialog, which ->
            launchGallery()
        }
        builder.setNegativeButton(getString(R.string.common_string_cancel)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Luncurkan galeri asli.
     */
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        launchGallery.launch(
            Intent.createChooser(intent, getString(R.string.profile_pic_dialog_title))
        )
    }

    /**
     * Peluncur galeri
     * panggilan balik saat kembali.
     */
    var launchGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val filePath = result.data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
                imageData = Utils.getEncodedString(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showProgress() {
        progressBar_cyclic.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar_cyclic.visibility = View.GONE
    }
}