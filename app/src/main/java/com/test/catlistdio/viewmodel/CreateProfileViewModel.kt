package com.test.catlistdio.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.models.entity.UserProfileEntity
import com.test.catlistdio.utils.DataCallback
import com.test.catlistdio.viewmodel.repository.CreateProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateProfileViewModel(appContext: SynchronyApp): ViewModel() {

    private var createProfileObserver: MutableLiveData<DataCallback<Boolean>>
    private var profileObserver: MutableLiveData<List<UserProfileEntity>>
    private var job: Job? = null

    @Inject
    lateinit var createProfileRepository: CreateProfileRepository

    init {
        appContext.getAppComponent().inject(this)
        createProfileObserver = MutableLiveData()
        profileObserver = MutableLiveData()
    }

    /**
     * mengembalikan pengamat untuk dipanggil saat penundaan selesai.
     *perlu diperhatikan aktivitasnya
     */
    fun getCreateProfileObserver(): MutableLiveData<DataCallback<Boolean>> {
        return createProfileObserver
    }

    /**
     * Kembalikan pengamat untuk mendaftar pada aktivitas ke perubahan pengamat.
     */
    fun getProfileObserver(): MutableLiveData<List<UserProfileEntity>> {
        return profileObserver
    }

    /**
     * Dipanggil untuk menyimpan data dalam db
     */
    fun createProfile(userEntity: UserProfileEntity) {
        job = viewModelScope.launch(Dispatchers.IO) {
            createProfileRepository.insertRecord(userEntity, createProfileObserver)
        }
    }

    /**
     * Baca data dari DB dan kirim pembaruan di UI.
     */
    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            createProfileRepository.getAllRecords(profileObserver)
        }
    }

    /**
     * Dipanggil saat model tampilan dihancurkan.
     */
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}