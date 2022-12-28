package com.test.catlistdio.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.test.catlistdio.models.CatBreedList
import com.test.catlistdio.network.IAPIService
import com.test.catlistdio.utils.DataCallback

class CatBreedsListRepository (val apiService: IAPIService) {
    /**
     * Lakukan panggilan api untuk mendapatkan data dari server.
     * dapatkan semua daftar ras kucing.
     */
    suspend fun getCatBreedsList(listObserver: MutableLiveData<DataCallback<CatBreedList>>) {
       /* val header = HashMap<String, String>()
        header.put(AppConstants.X_API_KEY, BuildConfig.API_KEY)*/
        val response = apiService.getCatBreedsListFromAPI()
        if(response.isSuccessful) {
            listObserver.postValue(DataCallback.success(response.body()))
        } else {
            listObserver.postValue(DataCallback.error(response.message(), null))
        }
    }
}