package com.test.catlistdio.di

import com.test.catlistdio.viewmodel.CatBreedListViewModelTest
import com.test.catlistdio.viewmodel.CatProfileListViewModelTest
import com.test.catlistdio.viewmodel.DaoTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface TestAppComponent: IAppComponent {
    fun inject(catBreedListViewModelTest: CatBreedListViewModelTest)
    fun inject(catProfileListViewModelTest: CatProfileListViewModelTest)
    fun inject(daoTest: DaoTest)
}