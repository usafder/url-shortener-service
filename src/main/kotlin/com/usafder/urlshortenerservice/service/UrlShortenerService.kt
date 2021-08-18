package com.usafder.urlshortenerservice.service

import com.google.common.hash.Hashing
import com.usafder.urlshortenerservice.dto.ShortUrlRequest
import com.usafder.urlshortenerservice.dto.ShortUrlResponse
import com.usafder.urlshortenerservice.exception.ServiceException
import com.usafder.urlshortenerservice.exception.enums.ServiceError
import com.usafder.urlshortenerservice.repository.UrlRepository
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class UrlShortenerService(
    @Autowired
    val urlRepository: UrlRepository
) {

    companion object {
        private const val HTTP = "http"
        private const val HTTPS = "https"
    }

    @Throws(ServiceException::class)
    fun getActualUrl(hashKey: String): String {
        var actualUrl: String?
        try {
            actualUrl = urlRepository.findBy(hashKey)
        } catch (e: Exception) {
            throw ServiceException(ServiceError.URL_FETCH_FAILED)
        }

        if (actualUrl.isNullOrEmpty()) {
            throw ServiceException(ServiceError.URL_NOT_FOUND)
        }

        return actualUrl
    }

    @Throws(ServiceException::class)
    fun generateShortUrl(request: ShortUrlRequest, baseUrl: String): ShortUrlResponse {
        val actualUrl = getActualUrlFrom(request)
        validateUrl(actualUrl)
        val hashKey = generateHashKey(actualUrl)

        try {
            saveKeyValueMapping(hashKey, actualUrl)
        } catch (e: Exception) {
            throw ServiceException(ServiceError.URL_SAVE_FAILED)
        }

        return ShortUrlResponse("${baseUrl}/${hashKey}")
    }

    private fun getActualUrlFrom(request: ShortUrlRequest) =
        if (request.actualUrl.startsWith(HTTP)) request.actualUrl else "${HTTPS}://${request.actualUrl}"

    private fun validateUrl(actualUrl: String) {
        val urlValidator = UrlValidator(arrayOf(HTTP, HTTPS))
        if (!urlValidator.isValid(actualUrl)) {
            throw ServiceException(ServiceError.INVALID_URL)
        }
    }

    private fun generateHashKey(actualUrl: String) = Hashing.murmur3_32()
        .hashString(actualUrl, StandardCharsets.UTF_8)
        .toString()

    private fun saveKeyValueMapping(key: String, value: String) = urlRepository.save(key, value)

}
