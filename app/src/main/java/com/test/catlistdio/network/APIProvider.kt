package com.test.catlistdio.network

import com.test.catlistdio.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIProvider{

    fun getRetroInstance(): Retrofit {

        /**
         * Aktifkan pencatatan Retrofit.
         */
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)

        /**
         * Kembalikan instance retrofit.
         */
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}