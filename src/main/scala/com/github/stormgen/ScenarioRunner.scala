package com.github.stormgen

import cats.effect.{ExitCode, IO, IOApp}
import com.github.stormgen.generator.Gen
import com.github.stormgen.scenario.ScenarioSettings

trait ScenarioRunner extends IOApp {
  implicit class Runner[K, V](scenarioSettings: ScenarioSettings[K, V])(implicit kGen:Gen[K], vGen: Gen[V]) {
    def run: IO[ExitCode] = scenarioSettings.compile.run.as(ExitCode.Success)
  }
}
