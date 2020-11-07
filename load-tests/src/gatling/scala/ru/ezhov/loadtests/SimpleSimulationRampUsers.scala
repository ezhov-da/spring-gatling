package ru.ezhov.loadtests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class SimpleSimulationRampUsers extends Simulation {
  private val httpProtocol = http
    .baseUrl("http://localhost:8080")

  private val scn = scenario("Simple atOnceUsers")
    .exec(
      http("/")
        .get("/")
        .check(status.is(200))
    )
    .pause(2)


  setUp(
    scn
      .inject(
        rampUsers(2) during (6 seconds) // 2 запроса
      )
      .protocols(httpProtocol)
  )
}
