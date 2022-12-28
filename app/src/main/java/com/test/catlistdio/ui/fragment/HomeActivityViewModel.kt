package com.test.catlistdio.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.models.entity.UserProfileEntity
import com.test.catlistdio.viewmodel.repository.CreateProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeActivityViewModel (appContext: SynchronyApp): ViewModel() {

    private var profileObserver: MutableLiveData<List<UserProfileEntity>>

    @Inject
    lateinit var createProfileRepository: CreateProfileRepository

    init {
        appContext.getAppComponent().inject(this)
        profileObserver = MutableLiveData()
    }

    /**
     * mengembalikan pengamat untuk dipanggil saat penundaan selesai.
     *perlu diperhatikan aktivitasnya
     */
    fun getProfileObserver(): MutableLiveData<List<UserProfileEntity>> {
        return profileObserver
    }

    /**
     * Dipanggil untuk menyimpan data dalam db
     */
    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            createProfileRepository.getAllRecords( profileObserver)
        }
    }
}