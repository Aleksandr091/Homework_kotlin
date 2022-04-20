package ru.chistov.homework.repository

import ru.chistov.homework.viewmodel.AppErrorState

fun interface OnErrorListener {
    fun onError(error: String)
}