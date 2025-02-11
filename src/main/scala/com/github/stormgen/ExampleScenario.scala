package com.github.stormgen

import cats.data.Chain
import cats.effect.{ExitCode, IO}
import com.github.stormgen.brokers.Kafka
import com.github.stormgen.scenario.{ConfigField, ScenarioApp, ScenarioSettings, WithBroker}

object ExampleScenario extends ScenarioApp {

  val scenarioSettings = ScenarioSettings.config()
    .name("test-scenario")
    .withBroker(Kafka(bootstrapServers = Seq("server1", "server2"), topics = Seq("topic1, topic2")))
    .withSteps(Chain.empty)
    .evaluate

  override def run(args: List[String]): IO[ExitCode] = {
    println(scenarioSettings)
    IO(ExitCode.Success)
  }
}
