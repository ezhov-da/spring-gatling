package ru.ezhov.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@SpringBootApplication
class SpringGatlingApplication

fun main(args: Array<String>) {
    runApplication<SpringGatlingApplication>(*args)
}

@RestController
class Controller {

    @GetMapping(path = ["/"])
    fun get(): String {
        return UUID.randomUUID().toString()
    }

    @GetMapping(path = ["/ping/{value}"])
    fun getPong(@PathVariable("value") value: String): String {
        return "pong [$value]"
    }
}