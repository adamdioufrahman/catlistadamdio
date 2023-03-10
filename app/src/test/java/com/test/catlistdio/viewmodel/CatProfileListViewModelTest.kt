package com.test.catlistdio.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.di.DaggerTestAppComponent
import com.test.catlistdio.di.TestAppModule
import com.test.catlistdio.models.entity.UserProfileEntity
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class CatProfileListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val context: SynchronyApp = mockk()

    lateinit var viewModel: CreateProfileViewModel

    val createProfileViewModelMocked: CreateProfileViewModel = mockk()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val component = DaggerTestAppComponent.builder()
            .appModule(TestAppModule(application = context)).build()

        component.inject(this)
        every { context.getAppComponent() } returns component
        viewModel = CreateProfileViewModel(context)
    }
    @Test
    fun test_get_profile() {
        val list = mutableListOf<UserProfileEntity>()
        list.add(getUserProfileEntity())
        every { createProfileViewModelMocked.getProfile() }answers {
            viewModel.getProfileObserver().value = list
        }
        createProfileViewModelMocked.getProfile()
        Assert.assertNotNull(viewModel.getProfileObserver().value)
        Assert.assertEquals( viewModel.getProfileObserver().value?.size, 1)
    }

    private fun getUserProfileEntity(): UserProfileEntity {
        return UserProfileEntity(1, "ravi", "kkk", "ravik@gmail.com", "9082345566", "MCA", "12Year", "Nice", null)
    }
}