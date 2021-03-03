package com.blankymunn3.recyclerswipemenu.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel:ViewModel() {
    val list: MutableLiveData<MutableList<String>> = MutableLiveData(ArrayList())
}