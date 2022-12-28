package com.test.catlistdio

import android.app.Application
import com.test.catlistdio.di.AppModule
import com.test.catlistdio.di.DaggerIAppComponent
import com.test.catlistdio.di.IAppComponent

class SynchronyApp: Application() {

    private lateinit var appComponent: IAppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerIAppComponent.builder()
            .appModule(AppModule(this@SynchronyApp))
            .build()
    }

    /**
     * Mengembalikan komponen DI
     */
    fun getAppComponent(): IAppComponent {
        return appComponent
    }
}