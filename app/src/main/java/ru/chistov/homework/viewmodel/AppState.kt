package ru.chistov.homework.viewmodel

import ru.chistov.homework.repository.Weather

sealed class AppState {
    object Loading : AppState()
    data class Success(val weatherListData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
}