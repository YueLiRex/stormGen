package com.github.stormgen.scenario

import cats.data.Chain
import com.github.stormgen.brokers.{Broker, NoBroker}

import scala.util.Random

case class ScenarioSettings(name: String, broker: Broker, steps: Chain[Step])

object ScenarioSettings {
  def apply(name: String): ScenarioSettings = ScenarioSettings(name = name, broker = NoBroker, steps = Chain.empty)

  def base = ScenarioSettings(name = Random.alphanumeric.take(16).mkString, broker = NoBroker, steps = Chain.empty)

  def config(): ConfigField = Empty
}
