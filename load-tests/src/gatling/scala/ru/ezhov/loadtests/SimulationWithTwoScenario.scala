package ru.ezhov.loadtests

import java.util.UUID

import io.gatling.core.Predef.{constantUsersPerSec, _}
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class SimulationWithTwoScenario extends Simulation {
  private val httpProtocol = http
    .baseUrl(Config.HOST)

  private val scnRoot = scenario("Simple /")
    .exec(
      http("/")
        .get("/")
        .check(status.is(200))
    )
    .pause(2)

  private val scnPing = scenario("Simple /ping/")
    .exec(
      http("/ping/")
        .get("/ping/" + UUID.randomUUID().toString)
        .check(status.is(200))
    )
    .pause(2)


  setUp(
    scnRoot
      .inject(
        rampUsersPerSec(0) to 6 during (6 seconds),
        constantUsersPerSec(2) during (6 seconds)
      )
      .protocols(httpProtocol)
      .throttle(
        jumpToRps(3),
        holdFor(10 seconds)
      ),
    scnPing
      .inject(
        rampUsersPerSec(0) to 6 during (6 seconds),
        constantUsersPerSec(2) during (6 seconds)
      )
      .protocols(httpProtocol)
      .throttle(
        jumpToRps(3),
        holdFor(10 seconds)
      ),
  )
}
