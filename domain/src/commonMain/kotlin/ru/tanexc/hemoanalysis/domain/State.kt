package ru.tanexc.hemoanalysis.domain

sealed interface State<out T> {
    data object Loading: State<Nothing>

    data class Success<T>(val data: T): State<T>

    data object Error: State<Nothing>
}