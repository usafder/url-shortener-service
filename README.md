# URL Shortener
A simple and easy to use URL shortening service built using Spring Boot, Kotlin and Redis.

## Motivation
Although there are many URL shortening services out there that can be used but they do come at a price.
So, why use their service when you can build your own and use it as you like !

## Getting Started
In order to get this service up and running locally, you need to follow the below steps:
- Clone the project by executing `git clone https://github.com/usafder/url-shortener-service.git` command in terminal.
- Make sure that the [Redis](https://redis.io/topics/quickstart) server is installed and running locally. In case you need to use another Redis server you can do so by overriding/configuring the `StringRedisTemplate` bean.
- After that open the cloned project using your preferred IDE, install the dependencies using maven and then run the project.
- If the project ran successfully then the service should now be available on [localhost:8080](http://localhost:8080) (default port is 8080 but you can update it in *application.yml*).

## Endpoints
- **POST /url** -> Used for generating the short URL
- **GET /url/{hash}** -> Used for redirecting the user to the actual URL
