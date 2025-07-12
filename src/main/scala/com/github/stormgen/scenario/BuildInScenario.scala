package com.github.stormgen.scenario

import fs2.kafka.KafkaSerializer

import scala.concurrent.duration.DurationInt
import scala.util.Random

object BuildInScenario {
  def smokeTest[K, V](keySerializer: KafkaSerializer[K], valueSerializer: KafkaSerializer[V], bootstrapServer: String, topic: String, numberPerSecond: Int) = {
    val duration = Random.between(5, 15)
    ScenarioSettings
      .create(keySerializer, valueSerializer)
      .withBootstrapServers(bootstrapServer)
      .withTopic(topic)
      .withPhase(Phase(duration.minutes, Rate(numberPerSecond, perDuration = 1.second)))
  }

  def averageLoadTest[K, V](keySerializer: KafkaSerializer[K], valueSerializer: KafkaSerializer[V], bootstrapServer: String, topic: String, numberPerSecond: Int) = {
    val duration = Random.between(30, 120)
    ScenarioSettings
      .create(keySerializer, valueSerializer)
      .withBootstrapServers(bootstrapServer)
      .withTopic(topic)
      .withPhase(Phase(duration.minutes, Rate(numberPerSecond, perDuration = 1.second)))
  }

  def spikeTest = ???

  def stressTest = ???

  def breakpointTest = ???

  def soakTest = ???
}
