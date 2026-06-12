package ru.tanexc.hemoanalysis.core.network

sealed interface RequestState<out T> {
    data class Success<T>(val data: T): RequestState<T>

    data class Error(val message: String): RequestState<Nothing>
}