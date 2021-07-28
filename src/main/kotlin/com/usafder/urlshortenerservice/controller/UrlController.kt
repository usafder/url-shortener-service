package com.usafder.urlshortenerservice.controller

import com.usafder.urlshortenerservice.dto.ShortUrlRequest
import com.usafder.urlshortenerservice.dto.ShortUrlResponse
import com.usafder.urlshortenerservice.service.UrlShortenerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/url")
class UrlController(
    @Autowired
    val urlShortenerService: UrlShortenerService
) {

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    fun generateShortUrl(
        @RequestBody shortUrlRequest: ShortUrlRequest,
        httpServletRequest: HttpServletRequest
    ): ShortUrlResponse {
        val baseUrl = httpServletRequest.requestURL.toString()
        return urlShortenerService.generateShortUrl(shortUrlRequest, baseUrl)
    }

    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping("/{hashKey}")
    fun redirectToActualUrl(@PathVariable hashKey: String, httpServletResponse: HttpServletResponse) {
        var actualUrl = urlShortenerService.getActualUrl(hashKey)
        httpServletResponse.setHeader("Location", actualUrl)
        httpServletResponse.status = 301
    }

}
