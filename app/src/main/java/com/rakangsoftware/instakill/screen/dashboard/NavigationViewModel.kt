package com.rakangsoftware.instakill.screen.dashboard

import android.arch.lifecycle.ViewModel
import com.rakangsoftware.instakill.utils.SingleLiveEvent

class NavigationViewModel : ViewModel() {
    val navigation = SingleLiveEvent<String>()

    fun launch(name: String) {
        navigation.value = name
    }
}