package com.usafder.urlshortenerservice.exception.enums

enum class ServiceError(val statusCode: Int, val message: String) {
    INVALID_URL(400, "Invalid URL provided."),
    URL_NOT_FOUND(404, "URL not found."),
    URL_FETCH_FAILED(500, "An error occurred while trying to fetch the URL."),
    URL_SAVE_FAILED(500, "An error occurred while trying to store the URL.")
}
