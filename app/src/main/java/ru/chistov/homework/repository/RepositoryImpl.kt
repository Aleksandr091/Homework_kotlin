package ru.chistov.homework.repository

class RepositoryImpl:Repository {
    override fun getWeatherFromServer():Weather {
        Thread{ Thread.sleep(1000L) }
        return Weather()
    }

    override fun getWeatherFromLocalStorage():Weather {
        return Weather()
    }
}