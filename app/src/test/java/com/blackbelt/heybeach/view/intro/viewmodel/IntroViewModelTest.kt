package com.blackbelt.heybeach.view.intro.viewmodel

import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.data.ResponseParser
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.domain.beaches.model.Beach
import com.blackbelt.heybeach.domain.model.ErrorModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException


@RunWith(MockitoJUnitRunner::class)
class IntroViewModelTest {

    @Mock
    private lateinit var mBeachesManager: IBeachesManager

    private val mIntroViewModel: IntroViewModel by lazy {
        IntroViewModel(mBeachesManager)
    }

    @Test
    fun test_empty_data_set() {
        Mockito.doAnswer {
            (it.arguments[1] as OnDataLoadedListener<List<Beach>>).onDataLoaded(listOf())
        }.`when`(mBeachesManager).loadBeaches(Mockito.anyInt(), Mockito.any())

        mIntroViewModel.onCreate()
        Assert.assertTrue(mIntroViewModel.getBeaches().isEmpty())
    }

    @Test
    fun test_not_empty_data_set() {

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

        mIntroViewModel.onCreate()
        Assert.assertTrue(mIntroViewModel.getBeaches().size == 6)
        Assert.assertTrue(mIntroViewModel.getBeaches()[0] is BeachItemViewModel)
    }

    @Test
    fun test_error() {
        Mockito.doAnswer {
            (it.arguments[1] as OnDataLoadedListener<List<Beach>>).onError(ErrorModel(), Throwable())
        }.`when`(mBeachesManager).loadBeaches(Mockito.anyInt(), Mockito.any())

        mIntroViewModel.onCreate()
        Assert.assertTrue(mIntroViewModel.getBeaches().isEmpty())
        Assert.assertTrue(mIntroViewModel.error == R.string.oops_something_went_wrong)
    }

    @Test
    fun test_network_error() {
        Mockito.doAnswer {
            (it.arguments[1] as OnDataLoadedListener<List<Beach>>).onError(ErrorModel(), IOException())
        }.`when`(mBeachesManager).loadBeaches(Mockito.anyInt(), Mockito.any())

        mIntroViewModel.onCreate()
        Assert.assertTrue(mIntroViewModel.getBeaches().isEmpty())
        Assert.assertTrue(mIntroViewModel.error == R.string.connection_error)
    }
}