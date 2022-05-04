package ru.chistov.homework.utils

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

class Utils {

}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}