package com.github.stormgen.scenario

import cats.effect.IOApp

trait ScenarioApp extends IOApp {
  val scenario = Scenario()

}
