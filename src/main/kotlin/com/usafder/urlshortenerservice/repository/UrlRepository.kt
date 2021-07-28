package com.usafder.urlshortenerservice.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class UrlRepository(
    @Autowired
    val redisTemplate: StringRedisTemplate
) {

    companion object {
        private val TWO_MIN_DURATION = Duration.ofMinutes(2)
    }

    fun save(key: String, value: String, timeout: Duration = TWO_MIN_DURATION) =
        redisTemplate.opsForValue().set(key, value, timeout)

    fun findBy(hashKey: String) = redisTemplate.opsForValue().get(hashKey)

}
