package ru.chistov.homework.repository

import ru.chistov.homework.viewmodel.DetailsViewModel

interface DetailsRepository {
    fun getWeatherDetails(city: City, myCallback: DetailsViewModel.MyCallback)
}