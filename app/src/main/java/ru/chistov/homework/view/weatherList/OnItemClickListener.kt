package ru.chistov.homework.view.weatherList

import ru.chistov.homework.repository.Weather

interface OnItemClickListener {
    fun onItemClick(weather: Weather)
}