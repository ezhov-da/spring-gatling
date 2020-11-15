package ru.ezhov.loadtests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class SimpleSimulationHeavisideUsers extends Simulation {
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
        heavisideUsers(15) during (6 seconds)
      )
      .protocols(httpProtocol)
  )
}
