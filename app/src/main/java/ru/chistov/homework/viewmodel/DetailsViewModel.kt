package ru.chistov.homework.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.chistov.homework.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryOkHttpImpl()
) : ViewModel() {
    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city, object : MyCallback {
            override fun onFailure(message: String) {
                liveData.postValue(DetailsState.Error(message))
            }

            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }
        })
    }

    interface MyCallback {
        fun onFailure(error: String)

        fun onResponse(weather: Weather)
    }

}