package com.github.stormgen.scenario

import cats.data.Chain
import com.github.stormgen.scenario.Scenario.Step
import org.apache.kafka.common.serialization.Serializer

import scala.concurrent.duration.FiniteDuration
import scala.util.Random

case class Scenario(name: String, brokerConfig: BrokerConfig, steps: Chain[Step], numberOfProducer: Int) extends ScenarioRunner {

  def name(name: String): Scenario = copy(name = name)

  def withBroker[K, V](bootstrapServers: List[String], topic: String, keySerializer: Serializer[K], valueSerializer: Serializer[V]): Scenario = {
    copy(brokerConfig = KafkaConfig(bootstrapServers, topic, keySerializer, valueSerializer))
  }

  def withNumberOfProducer(number: Int): Scenario = copy(numberOfProducer = number)

  def step(ratePerSecond: Int, duration: FiniteDuration): Scenario = copy(steps = this.steps.append(Step(ratePerSecond, duration)))
}

object Scenario {
  def apply() = new Scenario(
    name = Random.alphanumeric.take(6).mkString,
    brokerConfig = EmptyConfig,
    steps = Chain.empty,
    numberOfProducer = 1
  )

  case class Step(ratePerSecond: Int, duration: FiniteDuration)
}
