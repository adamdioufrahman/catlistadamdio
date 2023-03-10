package com.test.catlistdio.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.db.UserProfileDao
import com.test.catlistdio.di.DaggerTestAppComponent
import com.test.catlistdio.di.TestAppModule
import com.test.catlistdio.models.entity.UserProfileEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class DaoTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val context: SynchronyApp = mockk()

    val userProfileDaoMocked: UserProfileDao = mockk()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val component = DaggerTestAppComponent.builder()
            .appModule(TestAppModule(application = context)).build()

        component.inject(this)
        every { context.getAppComponent() } returns component
    }
    @Test
    fun test_get_profile() {
        val list = mutableListOf<UserProfileEntity>()
        list.add(getUserProfileEntity())
        every { userProfileDaoMocked.getAllProfile() } answers {
            list
        }
        val newList = userProfileDaoMocked.getAllProfile()
        Assert.assertNotNull(newList)
        Assert.assertEquals(newList.size, 1)
    }

    private fun getUserProfileEntity(): UserProfileEntity {
        return UserProfileEntity(1, "ravi", "kkk", "ravik@gmail.com", "9082345566", "MCA", "12Year", "Nice", null)
    }

}