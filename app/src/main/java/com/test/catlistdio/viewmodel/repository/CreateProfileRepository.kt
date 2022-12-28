package com.test.catlistdio.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.test.catlistdio.db.UserProfileDao
import com.test.catlistdio.models.entity.UserProfileEntity
import com.test.catlistdio.utils.DataCallback

class CreateProfileRepository(val userProfileDao: UserProfileDao) {
    /**
     * Menyimpan catatan dalam DB
     * kirim pembaruan ke pengamat.
     */
    suspend fun insertRecord(userEntity: UserProfileEntity, createProfileObserver: MutableLiveData<DataCallback<Boolean>>) {
        userProfileDao.insertRecords(userEntity)
        createProfileObserver.postValue(DataCallback.success(true))
    }

    /**
     * Baca catatan dari DB
     * kirim pembaruan ke pengamat
     */
    suspend fun getAllRecords(createProfileObserver: MutableLiveData<List<UserProfileEntity>>) {
        val list = userProfileDao.getAllProfile()
        createProfileObserver.postValue(list)
    }
}