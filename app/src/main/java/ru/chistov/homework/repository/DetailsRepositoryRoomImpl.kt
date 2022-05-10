package ru.chistov.homework.repository

import ru.chistov.homework.MyApp
import ru.chistov.homework.utils.convertHistoryEntityToWeather
import ru.chistov.homework.utils.convertWeatherToEntity
import ru.chistov.homework.viewmodel.DetailsViewModel
import ru.chistov.homework.viewmodel.HistoryViewModel

class DetailsRepositoryRoomImpl : DetailsRepository, DetailsRepositoryForAll, DetailsRepositoryAdd {
    override fun getWeatherDetails(city: City, myCallback: DetailsViewModel.MyCallback) {
        val list = convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
        myCallback.onResponse(list.last())


    }

    override fun getAllWeatherDetails(myCallback: HistoryViewModel.MyCallbackForAll) {
        myCallback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
    }

}