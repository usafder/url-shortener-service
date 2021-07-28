package com.usafder.urlshortenerservice.exception

import com.usafder.urlshortenerservice.exception.enums.ServiceError

class ServiceException : RuntimeException {
    var statusCode: Int

    constructor(serviceError: ServiceError) : super(serviceError.message) {
        this.statusCode = serviceError.statusCode
    }
}
