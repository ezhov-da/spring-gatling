package ru.ezhov.loadtests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class SimpleSimulationRampUsersPerSec extends Simulation {
  private val httpProtocol = http
    .baseUrl(Config.HOST)

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
        rampUsersPerSec(0) to 6 during (6 seconds) //18 запросов
      )
      .protocols(httpProtocol)
  )
}
