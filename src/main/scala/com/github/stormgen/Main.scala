package com.github.stormgen

import scala.concurrent.duration.DurationInt

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import com.github.stormgen.scenario.Phase
import com.github.stormgen.scenario.Rate
import com.github.stormgen.scenario.ScenarioSettings
import org.apache.kafka.common.serialization.StringSerializer


object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    ScenarioSettings
      .create(new StringSerializer, new StringSerializer)
      .withBootstrapServers("localhost:9092")
      .withTopic("test")
      .withPhase(Phase(10.seconds, Rate(3, 1.second)))
      .withPhase(Phase(5.seconds, Rate(2, 1.second)))
      .compile
      .run.as(ExitCode.Success)
  }
}
