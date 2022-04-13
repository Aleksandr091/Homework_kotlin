package ru.chistov.homework.repository

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        Thread { Thread.sleep(1000L) }
        return Weather()
    }

    override fun getWorldWeatherFromLocalStorage() = getWorldCities()


    override fun getRussianWeatherFromLocalStorage() = getRussianCities()

}