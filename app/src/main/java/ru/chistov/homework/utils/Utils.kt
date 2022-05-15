package ru.chistov.homework.utils

import ru.chistov.homework.domain.room.HistoryEntity
import ru.chistov.homework.repository.City
import ru.chistov.homework.repository.Weather
import ru.chistov.homework.repository.dto.FactDTO
import ru.chistov.homework.repository.dto.WeatherDTO
import ru.chistov.homework.repository.getDefaultCity

const val KEY_BUNDLE_WEATHER = "key"
const val KEY_BUNDLE_LAT = "lat"
const val KEY_BUNDLE_LON = "lon"
const val KEY_WAVE = "KEY_WAVE"
const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "KEY_BUNDLE_SERVICE_BROADCAST_WEATHER"
const val API_KEY = "X-Yandex-API-Key"
const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
const val YANDEX_DOMAIN_HARD_MODE = "http://212.86.114.27/"
const val YANDEX_ENDPOINT = "v2/informers?"
const val KEY_BUNDLE_SERVICE_MESSAGE = "KEY_BUNDLE_SERVICE_MESSAGE"
const val KEY_BUNDLE_ACTIVITY_MESSAGE = "key1"
const val KEY_MESSAGE_ERROR_SERVERSIDE = "messageErrorServerside"
const val KEY_MESSAGE_ERROR_CLIENTSIDE = "messageErrorClientside"
const val KEY_MESSAGE_ERROR = "messageError"
const val KEY_SP_FILE_NAME_1 = "FILE_NAME_1"
const val KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN = "KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN"
const val REQUEST_CODE = 89
const val REQUEST_CODE_CALL = 879

class Utils {

}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temperature, weather.feelsLike, weather.icon)
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(
            City(it.city, 0.0, 0.0),
            it.temperature,
            it.feelsLike,
            it.icon
        ) // TODO HW было бы здорово научиться хранить в БД lat lon
    }
}