package com.example.finalmanager.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarViewModel : ViewModel() {
    val teamName = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val opponent = MutableLiveData<String>()
    val scoreFirst = MutableLiveData<Int>()
    val scoreSecond = MutableLiveData<Int>()
    val isPlayed = MutableLiveData<Boolean>()
    val addGame = MutableLiveData<Boolean>()
    val changedId = MutableLiveData<Int>()
    val changed = MutableLiveData<Boolean>()
    val changeGame = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val deleteGame = MutableLiveData<Boolean>()
    val gamesPlayed = MutableLiveData<Int>()
    val allGamesDate = MutableLiveData<List<String>>()
    init {
        scoreFirst.value = 0
        scoreSecond.value = 0
        gamesPlayed.value = 0
        allGamesDate.value = listOf("str")
    }
}