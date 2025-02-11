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

sealed trait ConfigField {

  def name(name: String): ConfigField = this.and(Name(name))
  def withBroker(broker: Broker): ConfigField = this.and(WithBroker(broker))
  def withSteps(steps: Chain[Step]): ConfigField = this.and(WithSteps(steps))

  def and(next: ConfigField) = And(this, next)

  def evaluate(scenarioSettings: ScenarioSettings = ScenarioSettings.base): ScenarioSettings = {
    this match {
      case Name(name) => scenarioSettings.copy(name = name)
      case WithBroker(broker) => scenarioSettings.copy(broker = broker)
      case WithSteps(steps) => scenarioSettings.copy(steps = steps)
      case Empty => ScenarioSettings.base

      case And(first, second) =>
        second.evaluate(first.evaluate(scenarioSettings))
    }
  }

  def evaluate: ScenarioSettings = evaluate(ScenarioSettings.base)
}

case object Empty extends ConfigField

case class Name(name: String) extends ConfigField

case class WithBroker(broker: Broker) extends ConfigField

case class WithSteps(steps: Chain[Step]) extends ConfigField

case class And(first: ConfigField, second: ConfigField) extends ConfigField

object ConfigField {
  def empty: ConfigField = Empty
}

