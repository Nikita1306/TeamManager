package com.example.finalmanager.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val message = MutableLiveData<String>()
    val surname = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val position = MutableLiveData<String>()
    val goals = MutableLiveData<Int>()
    val assists = MutableLiveData<Int>()
    val deletePlayer = MutableLiveData<Boolean>()
    val addPlayer = MutableLiveData<Boolean>()
    val changeSurname = MutableLiveData<String>()
    val changeName = MutableLiveData<String>()
    val changePosition = MutableLiveData<String>()
    val changePlayer = MutableLiveData<Boolean>()
    val changed = MutableLiveData<Boolean>()
    val changedId = MutableLiveData<Int>()
    init {
        addPlayer.value = false
        goals.value = 0
        assists.value = 0
    }
}