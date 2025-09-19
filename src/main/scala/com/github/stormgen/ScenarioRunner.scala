package com.github.stormgen

import cats.effect.Fiber
import cats.effect.IOApp
import com.github.stormgen.generator.Gen
import com.github.stormgen.scenario.ScenarioSettings

trait ScenarioRunner extends IOApp {
  implicit class Runner[F[_], K, V](scenarioSettings: ScenarioSettings[F, K, V])(implicit kGen: Gen[K], vGen: Gen[V]) {
    def run: F[Fiber[F, Throwable, Unit]] = scenarioSettings.compile.start
  }
}
