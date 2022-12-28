package com.test.catlistdio.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.models.CatBreedList
import com.test.catlistdio.utils.AppConnectivityManager
import com.test.catlistdio.utils.DataCallback
import com.test.catlistdio.viewmodel.repository.CatBreedsListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatBreedsListViewModel (appContext: SynchronyApp): ViewModel() {

    private var catsBreedListObserver: MutableLiveData<DataCallback<CatBreedList>>
    private var job: Job? = null

    @Inject
    lateinit var catBreedsListRepo: CatBreedsListRepository

    @Inject
    lateinit var appConnectivityManager: AppConnectivityManager

    init {
        appContext.getAppComponent().inject(this)
        catsBreedListObserver = MutableLiveData()
    }
    /**
     * Periksa konektivitas internet.
     */
    fun hasInternetConnection(): Boolean {
        return appConnectivityManager.isConnectedToInternet()
    }

    /**
     * mengembalikan pengamat untuk mengamati hasilnya
     * perlu mendaftar pada aktivitas
     */
    fun getCatBreedsListObserver(): MutableLiveData<DataCallback<CatBreedList>> {
        return catsBreedListObserver
    }

    /**
     * Lakukan panggilan api melalui repositori
     */
    fun getCatBreedsList() {
        catsBreedListObserver.postValue(DataCallback.loading(null))
        job = viewModelScope.launch(Dispatchers.IO) {
           catBreedsListRepo.getCatBreedsList(catsBreedListObserver)
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