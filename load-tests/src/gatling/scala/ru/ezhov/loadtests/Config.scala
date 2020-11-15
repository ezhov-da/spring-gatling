package ru.ezhov.loadtests

object Config {
  val HOST = Properties.getProperty("HOST", "http://localhost:8080")
}
