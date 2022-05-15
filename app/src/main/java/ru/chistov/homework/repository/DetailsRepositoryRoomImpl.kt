package ru.chistov.homework.repository

import ru.chistov.homework.MyApp
import ru.chistov.homework.utils.convertHistoryEntityToWeather
import ru.chistov.homework.utils.convertWeatherToEntity
import ru.chistov.homework.viewmodel.DetailsViewModel
import ru.chistov.homework.viewmodel.HistoryViewModel

class DetailsRepositoryRoomImpl : DetailsRepository, DetailsRepositoryForAll, DetailsRepositoryAdd {
    override fun getWeatherDetails(city: City, myCallback: DetailsViewModel.MyCallback) {
        Thread{val list = convertHistoryEntityToWeather(
            MyApp.getHistoryDao().getHistoryForCity(city.name))
            myCallback.onResponse(list.last())}.start()
    }

    override fun getAllWeatherDetails(myCallback: HistoryViewModel.MyCallbackForAll) {
        Thread{myCallback.onResponse(convertHistoryEntityToWeather(
            MyApp.getHistoryDao().getAll()))}.start()
    }

    override fun addWeather(weather: Weather) {
        Thread{MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))}.start()
    }

}