package ru.chistov.homework.repository

import ru.chistov.homework.viewmodel.HistoryViewModel

interface DetailsRepositoryForAll {
    fun getAllWeatherDetails(myCallback: HistoryViewModel.MyCallbackForAll)
}