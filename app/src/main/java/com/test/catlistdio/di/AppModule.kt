package com.test.catlistdio.di

import android.content.Context
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.db.AppDatabase
import com.test.catlistdio.db.UserProfileDao
import com.test.catlistdio.network.APIProvider
import com.test.catlistdio.network.IAPIService
import com.test.catlistdio.utils.AppConnectivityManager
import com.test.catlistdio.viewmodel.repository.CatBreedsListRepository
import com.test.catlistdio.viewmodel.repository.CreateProfileRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
open class AppModule(private val application: SynchronyApp) {
    @Singleton
    @Provides
    fun getApiServiceInstance(retrofit: Retrofit): IAPIService {
        return retrofit.create(IAPIService::class.java)
    }

    @Provides
    @Singleton
    open fun getApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    open fun getConnectivityManager(): AppConnectivityManager {
        return AppConnectivityManager(getApplicationContext())
    }

    @Singleton
    @Provides
    open fun getUserProfileDao(appDatabase: AppDatabase): UserProfileDao {
        return appDatabase.getUserProfileDao()
    }

    @Singleton
    @Provides
    open fun getRoomDbInstance(context: Context): AppDatabase {
        return AppDatabase.getAppDBInstance(context)
    }

    @Singleton
    @Provides
    open fun getAPIProviderInstance(): Retrofit {
        return APIProvider.getRetroInstance()
    }

    @Singleton
    @Provides
    fun getCatBreedsListRepository(apiService: IAPIService): CatBreedsListRepository {
        return CatBreedsListRepository(apiService)
    }

    @Singleton
    @Provides
    fun getCreateProfileRepository(userProfileDao: UserProfileDao): CreateProfileRepository {
        return CreateProfileRepository(userProfileDao)
    }
}