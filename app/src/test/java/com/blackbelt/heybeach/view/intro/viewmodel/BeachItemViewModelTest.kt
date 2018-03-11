package com.blackbelt.heybeach.view.intro.viewmodel

import com.blackbelt.heybeach.domain.beaches.model.Beach
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BeachItemViewModelTest {

    @Test
    fun test_beach_item_view_model() {
        val beach = Beach("id", "name", "url", "100", "100")
        val beachItemViewModel = BeachItemViewModel(beach)
        Assert.assertTrue(beachItemViewModel.getName()?.equals(beach.name) ?: false)
        Assert.assertTrue(beachItemViewModel.getBeachUrl()?.equals(beach.url) ?: false)
    }

}