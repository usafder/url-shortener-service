package com.usafder.urlshortenerservice.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ErrorResponse(
    @JsonProperty("statusCode")
    val statusCode: Number,
    @JsonProperty("message")
    val message: String
)
