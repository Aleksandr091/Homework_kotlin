package ru.chistov.homework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.chistov.homework.repository.DetailsRepositoryRoomImpl
import ru.chistov.homework.repository.Weather

class HistoryViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: DetailsRepositoryRoomImpl = DetailsRepositoryRoomImpl()

) : ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getAll() {
        repository.getAllWeatherDetails(object : MyCallbackForAll {
            override fun onFailure(error: String) {

            }

            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(AppState.Success(listWeather))
            }

        })
    }

    interface MyCallbackForAll {
        fun onFailure(error: String)

        fun onResponse(listWeather: List<Weather>)
    }
}