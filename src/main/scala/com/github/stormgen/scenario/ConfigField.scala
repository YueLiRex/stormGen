package com.github.stormgen.scenario

import cats.data.Chain
import com.github.stormgen.brokers.Broker

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

  def build: ScenarioSettings = evaluate(ScenarioSettings.base)
}

case object Empty extends ConfigField

case class Name(name: String) extends ConfigField

case class WithBroker(broker: Broker) extends ConfigField

case class WithSteps(steps: Chain[Step]) extends ConfigField

case class And(first: ConfigField, second: ConfigField) extends ConfigField

object ConfigField {
  def empty: ConfigField = Empty
}
