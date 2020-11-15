package ru.ezhov.loadtests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class SimpleSimulationAtOnceUsers extends Simulation {
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
        atOnceUsers(5)
      )
      .protocols(httpProtocol)
      .throttle(
        jumpToRps(3),
        holdFor(5 seconds)
      )
  )
}
