package ru.chistov.homework.viewmodel

import ru.chistov.homework.repository.Weather

sealed class DetailsState {
    object Loading : DetailsState()
    data class Success(val weather: Weather) : DetailsState()
    data class Error(val error: String) : DetailsState()
}