package com.acmpo6ou.stardict.dicts_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DictsViewModel : ViewModel() {
    val dicts = MutableLiveData<List<String>>(listOf())
}
