package com.usafder.urlshortenerservice.service

import com.usafder.urlshortenerservice.dto.ShortUrlRequest
import com.usafder.urlshortenerservice.dto.ShortUrlResponse
import com.usafder.urlshortenerservice.exception.ServiceException
import com.usafder.urlshortenerservice.repository.UrlRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UrlShortenerServiceTest {

    private val HASH_KEY = "test1234"
    private val ACTUAL_URL = "google.com"
    private val BASE_URL = "service-test.com"
    private val EMPTY_STRING = ""

    @MockK
    private lateinit var urlRepository: UrlRepository

    @InjectMockKs
    private lateinit var urlShortenerService: UrlShortenerService

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `should get the actual URL successfully`() {
        every { urlRepository.findBy(HASH_KEY) }.returns(ACTUAL_URL)

        val expectedValue = ACTUAL_URL
        val actualValue = urlShortenerService.getActualUrl(HASH_KEY)
        assertEquals(expectedValue, actualValue)
    }

    @Test(expected = ServiceException::class)
    fun `should throw exception if something went wrong in repository layer while fetching`() {
        every { urlRepository.findBy(HASH_KEY) }.throws(Exception())
        urlShortenerService.getActualUrl(HASH_KEY)
    }

    @Test(expected = ServiceException::class)
    fun `should throw exception if no URL is found`() {
        every { urlRepository.findBy(HASH_KEY) }.returns(EMPTY_STRING)
        urlShortenerService.getActualUrl(HASH_KEY)
    }

    @Test
    fun `should generate short URL successfully`() {
        val shortUrlRequest = ShortUrlRequest(ACTUAL_URL)
        val hashKeySlot = slot<String>()

        justRun { urlRepository.save(capture(hashKeySlot), any()) }

        val actualResponse = urlShortenerService.generateShortUrl(shortUrlRequest, BASE_URL)
        val expectedResponse = ShortUrlResponse("${BASE_URL}/${hashKeySlot.captured}")
        assertEquals(expectedResponse.shortUrl, actualResponse.shortUrl)
    }

    @Test(expected = ServiceException::class)
    fun `should throw exception on invalid url`() {
        val shortUrlRequest = ShortUrlRequest(EMPTY_STRING)
        urlShortenerService.generateShortUrl(shortUrlRequest, BASE_URL)
    }

    @Test(expected = ServiceException::class)
    fun `should throw exception if something went wrong in repository layer while saving`() {
        val shortUrlRequest = ShortUrlRequest(ACTUAL_URL)

        every { urlRepository.save(any(), any()) }.throws(Exception())

        urlShortenerService.generateShortUrl(shortUrlRequest, BASE_URL)
    }

}
