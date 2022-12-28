package com.test.catlistdio.di

import com.test.catlistdio.ui.fragment.HomeActivityViewModel
import com.test.catlistdio.viewmodel.CatBreedsListViewModel
import com.test.catlistdio.viewmodel.CreateProfileViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface IAppComponent {
    fun inject(catBreedsListViewModel: CatBreedsListViewModel)
    fun inject(createProfileViewModel: CreateProfileViewModel)
    fun inject(homeActivityViewModel: HomeActivityViewModel)
}