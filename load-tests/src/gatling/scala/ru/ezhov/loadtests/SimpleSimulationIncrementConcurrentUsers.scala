package ru.ezhov.loadtests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class SimpleSimulationIncrementConcurrentUsers extends Simulation {
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
        incrementConcurrentUsers(5) // Int
          .times(5)
          .eachLevelLasting(10 seconds)
          .separatedByRampsLasting(10 seconds)
          .startingFrom(10) // Int
      )
      .protocols(httpProtocol)
  )
}
