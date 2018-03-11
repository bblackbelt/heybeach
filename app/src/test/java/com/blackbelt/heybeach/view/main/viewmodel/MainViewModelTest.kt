package com.blackbelt.heybeach.view.main.viewmodel

import com.blackbelt.heybeach.data.ResponseParser
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.domain.beaches.model.Beach
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.IUserManager
import com.blackbelt.heybeach.domain.user.model.UserModel
import com.blackbelt.heybeach.view.View
import com.blackbelt.heybeach.view.intro.viewmodel.BeachItemViewModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {


    @Mock
    private lateinit var mBeachesManager: IBeachesManager

    @Mock
    private lateinit var mUserManager: IUserManager

    @Mock
    private lateinit var mListener: View<Any>

    private val mMainViewModel: MainViewModel by lazy {
        MainViewModel(mUserManager, mBeachesManager)
    }

    @Test
    fun test_user() {
        val userModel = UserModel("testId", "test@test.com")
        Mockito.doAnswer {
            (it.arguments[0] as OnDataLoadedListener<UserModel>).onDataLoaded(userModel)
        }.`when`(mUserManager).getUser(Mockito.any())

        mMainViewModel.onCreate()
        Assert.assertTrue(userModel.email.equals(mMainViewModel.getUserEmail()))
    }

    @Test
    fun test_user_failed() {
        Mockito.doAnswer {
            (it.arguments[0] as OnDataLoadedListener<UserModel>).onError(ErrorModel(), Throwable())
        }.`when`(mUserManager).getUser(Mockito.any())

        mMainViewModel.onCreate()
        Assert.assertTrue(mMainViewModel.getUserEmail().isNullOrEmpty())
    }

    @Test
    fun test_fetch_first_page() {
        Mockito.doAnswer {
            val json = javaClass.classLoader.getResourceAsStream("beaches.json")
                    .bufferedReader().use { it.readText() }
            val beaches = mutableListOf<Beach>()
            val beachesResponseModel = ResponseParser().toBeachList(json)
            beachesResponseModel.forEach {
                beaches.add(Beach(it.id, it.name, it.url, it.width, it.height))
            }
            (it.arguments[1] as OnDataLoadedListener<List<Beach>>).onDataLoaded(beaches)
        }.`when`(mBeachesManager).loadBeaches(Mockito.anyInt(), Mockito.any())

        mMainViewModel.nextPage = mMainViewModel.nextPage
        Assert.assertTrue(mMainViewModel.items.size == 6)
        Assert.assertTrue(mMainViewModel.items[0] is BeachItemViewModel)
        Assert.assertFalse(mMainViewModel.firstLoading)
        Assert.assertFalse(mMainViewModel.items.contains(mMainViewModel.mProgressLoader))
    }

    @Test
    fun test_handle_loading_first_page() {
        mMainViewModel.nextPage.setCurrentPage(1)
        mMainViewModel.handleLoading(true)
        Assert.assertTrue(mMainViewModel.firstLoading)
        Assert.assertFalse(mMainViewModel.items.contains(mMainViewModel.mProgressLoader))
    }

    @Test
    fun test_handle_loading_other_pages() {
        mMainViewModel.nextPage.setCurrentPage(2)
        mMainViewModel.handleLoading(true)
        Assert.assertFalse(mMainViewModel.firstLoading)
        Assert.assertTrue(mMainViewModel.items.contains(mMainViewModel.mProgressLoader))

        mMainViewModel.handleLoading(false)

        Assert.assertFalse(mMainViewModel.items.contains(mMainViewModel.mProgressLoader))
    }

    @Test
    fun test_logout_successful() {
        mMainViewModel.mListener = mListener
        Mockito.doAnswer {
            (it.arguments[0] as OnDataLoadedListener<Boolean>).onDataLoaded(true)
        }.`when`(mUserManager).logout(Mockito.any())

        mMainViewModel.logOut()
        Mockito.verify(mListener).onDataLoaded(true)
    }


    @Test
    fun test_logout_error() {
        mMainViewModel.mListener = mListener

        val throwable = Throwable()
        val errorModel = ErrorModel()

        Mockito.doAnswer {
            (it.arguments[0] as OnDataLoadedListener<Boolean>).onError(errorModel, throwable)
        }.`when`(mUserManager).logout(Mockito.any())

        mMainViewModel.logOut()
        Mockito.verify(mListener).onError(errorModel, throwable)
    }
}