package ru.chistov.homework.repository

class RepositoryImpl:Repository {
    override fun getWeatherFromServer():Weather {
        Thread{ Thread.sleep(1000L) }
        return Weather()
    }

    override fun getWorldWeatherFromLocalStorage():List<Weather> {
        return getWorldCities()
    }

    override fun getRussianWeatherFromLocalStorage():List<Weather> {
        return getRussianCities()
    }
}