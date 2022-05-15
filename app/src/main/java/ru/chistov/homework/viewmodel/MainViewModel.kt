package ru.chistov.homework.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.chistov.homework.repository.RepositoryImpl
import ru.chistov.homework.view.MainActivity

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }


    fun getWeatherRussian() = getWeather(true)

    fun getWeatherWorld() = getWeather(false)

    private fun getWeather(isRussian: Boolean) {
        Thread {
            liveData.postValue(AppState.Loading)

            val answer = if (isRussian) repository.getRussianWeatherFromLocalStorage()
            else repository.getWorldWeatherFromLocalStorage()
            liveData.postValue(AppState.Success(answer))
        }.start()
    }

}