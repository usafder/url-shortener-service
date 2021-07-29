package com.usafder.urlshortenerservice.repository

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.Duration

class UrlRepositoryTest {

    private val KEY = "key"
    private val VALUE = "value"
    private val TWO_MIN_DURATION = Duration.ofMinutes(2)

    @MockK
    private lateinit var redisTemplate: StringRedisTemplate

    @InjectMockKs
    private lateinit var urlRepository: UrlRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `should save record successfully`() {
        justRun { redisTemplate.opsForValue().set(KEY, VALUE, TWO_MIN_DURATION) }

        val actualResponse = urlRepository.save(KEY, VALUE, TWO_MIN_DURATION)
        val expectedResponse = Unit

        verify(exactly = 1) { redisTemplate.opsForValue().set(KEY, VALUE, TWO_MIN_DURATION) }
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `should assign default timeout of 2 minutes and save record successfully`() {
        justRun { redisTemplate.opsForValue().set(KEY, VALUE, TWO_MIN_DURATION) }

        val actualResponse = urlRepository.save(KEY, VALUE)
        val expectedResponse = Unit

        verify(exactly = 1) { redisTemplate.opsForValue().set(KEY, VALUE, TWO_MIN_DURATION) }
        assertEquals(expectedResponse, actualResponse)
    }

    @Test(expected = Exception::class)
    fun `should throw exception while saving`() {
        every { redisTemplate.opsForValue().set(KEY, VALUE, TWO_MIN_DURATION) }.throws(Exception())
        urlRepository.save(KEY, VALUE, TWO_MIN_DURATION)
    }

    @Test
    fun `should retrieve value successfully`() {
        every { redisTemplate.opsForValue().get(KEY) }.returns(VALUE)

        val actualResponse = urlRepository.findBy(KEY)
        val expectedResponse = VALUE
        verify(exactly = 1) { redisTemplate.opsForValue().get(KEY) }
        assertEquals(expectedResponse, actualResponse)
    }

    @Test(expected = Exception::class)
    fun `should throw exception while retrieving value`() {
        every { redisTemplate.opsForValue().get(KEY) }.throws(Exception())

        val actualResponse = urlRepository.findBy(KEY)
        val expectedResponse = VALUE
        assertEquals(expectedResponse, actualResponse)
    }
}
