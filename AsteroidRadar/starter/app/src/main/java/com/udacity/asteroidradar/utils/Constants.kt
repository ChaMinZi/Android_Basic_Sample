package com.udacity.asteroidradar.utils

import java.time.LocalDate

object Constants {
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov"
    const val API_KEY = "API_KEY"

    fun getToday(): String {
        return LocalDate.now().toString()
    }

    fun getAfter7Days(): String {
        return LocalDate.now().plusDays(7).toString()
    }
}