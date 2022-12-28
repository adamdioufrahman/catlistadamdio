package com.test.catlistdio.viewmodel.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    private var splashObserver: MutableLiveData<Boolean>
    private var job: Job? = null
    init {
        splashObserver = MutableLiveData()
    }

    /**
     * mengembalikan pengamat untuk dipanggil saat penundaan selesai.
     *perlu diperhatikan aktivitasnya
     */
    fun getSplashObserver(): LiveData<Boolean> {
        return splashObserver
    }

    /**
     * Dipanggil untuk menghasilkan jeda 4 detik untuk menyelesaikan animasi pada splash
     */
    fun startSplashDelay() {
        job = viewModelScope.launch(Dispatchers.IO) {
            Thread.sleep(6000)
            splashObserver.postValue(true)
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