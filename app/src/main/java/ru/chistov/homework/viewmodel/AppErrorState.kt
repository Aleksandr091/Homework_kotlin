package ru.chistov.homework.viewmodel

import ru.chistov.homework.repository.Weather

sealed class AppErrorState {
    data class ErrorClientSide(val error: String ) : AppErrorState()
    data class ErrorServerSide(val error: String) : AppErrorState()
    data class Error(val error: String) : AppErrorState()
}