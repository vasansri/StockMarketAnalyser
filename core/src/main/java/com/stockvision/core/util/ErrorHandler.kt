package com.stockvision.core.util

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ErrorHandler {
    fun handleError(throwable: Throwable): String {
        return when (throwable) {
            is IOException -> "Network error. Please check your connection."
            is HttpException -> {
                when (throwable.code()) {
                    401 -> "Unauthorized. Please login again."
                    403 -> "Forbidden access."
                    404 -> "Resource not found."
                    500 -> "Server error. Please try again later."
                    else -> "Something went wrong."
                }
            }
            is SocketTimeoutException -> "Connection timed out."
            else -> throwable.localizedMessage ?: "An unexpected error occurred."
        }
    }
}
