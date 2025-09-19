package com.github.stormgen.scenario

import scala.concurrent.duration._
import scala.util.Random

import cats.effect.Async
import fs2.kafka.KafkaSerializer

object BuildInScenario {
  def smokeTest[F[_]: Async, K, V](
      keySerializer: KafkaSerializer[K],
      valueSerializer: KafkaSerializer[V],
      bootstrapServer: String,
      topic: String,
      numberPerSecond: Int
  ): ScenarioSettings[F, K, V] = {
    val duration = Random.between(10, 30)

    ScenarioSettings
      .empty(keySerializer, valueSerializer)
      .withBootstrapServers(bootstrapServer)
      .withTopic(topic)
      .withPhase(Phase(duration.minutes, Rate(numberPerSecond, perDuration = 1.second)))
  }

  def averageLoadTest[F[_]: Async, K, V](
      keySerializer: KafkaSerializer[K],
      valueSerializer: KafkaSerializer[V],
      bootstrapServer: String,
      topic: String,
      numberPerSecond: Int
  ): ScenarioSettings[F, K, V] = {
    val duration = Random.between(10, 20)

    ScenarioSettings
      .empty(keySerializer, valueSerializer)
      .withBootstrapServers(bootstrapServer)
      .withTopic(topic)
      .withPhase(Phase(duration.minutes, Rate(numberPerSecond, perDuration = 1.second)))
  }

  def soakTest[F[_]: Async, K, V](
      keySerializer: KafkaSerializer[K],
      valueSerializer: KafkaSerializer[V],
      bootstrapServer: String,
      topic: String,
      numberPerSecond: Int
  ): ScenarioSettings[F, K, V] = {
    val duration = Random.between(60, 90)

    ScenarioSettings
      .empty(keySerializer, valueSerializer)
      .withBootstrapServers(bootstrapServer)
      .withTopic(topic)
      .withPhase(Phase(duration.minutes, Rate(numberPerSecond, 1.second)))
  }

  def stressTest[F[_]: Async, K, V](
      keySerializer: KafkaSerializer[K],
      valueSerializer: KafkaSerializer[V],
      bootstrapServer: String,
      topic: String,
      numberPerSecond: Int
  ): ScenarioSettings[F, K, V] = {
    val duration = Random.between(10, 20)

    ScenarioSettings
      .empty(keySerializer, valueSerializer)
      .withBootstrapServers(bootstrapServer)
      .withTopic(topic)
      .withPhase(Phase(duration.minutes, Rate(numberPerSecond, perDuration = 1.second)))
  }

  def spikeTest[F[_]: Async, K, V](
      keySerializer: KafkaSerializer[K],
      valueSerializer: KafkaSerializer[V],
      bootstrapServer: String,
      topic: String,
      lowLoadNumberPerSecond: Int,
      highLoadNumberPerSecond: Int
  ): ScenarioSettings[F, K, V] = {
    val lowDuration  = Random.between(30, 90)
    val highDuration = Random.between(30, 60)

    ScenarioSettings
      .empty(keySerializer, valueSerializer)
      .withBootstrapServers(bootstrapServer)
      .withTopic(topic)
      .withPhase(Phase(lowDuration.seconds, Rate(lowLoadNumberPerSecond, 1.second)))
      .withPhase(Phase(highDuration.seconds, Rate(highLoadNumberPerSecond, 1.second)))
  }

  def breakpointTest[F[_]: Async, K, V](
      keySerializer: KafkaSerializer[K],
      valueSerializer: KafkaSerializer[V],
      bootstrapServer: String,
      topic: String,
      stepDuration: FiniteDuration,
      stepNumber: Int,
      totalDuration: FiniteDuration
  ): ScenarioSettings[F, K, V] = {
    val numberOfRound = totalDuration.toSeconds / stepDuration.toSeconds
    val phases: Seq[Phase] = (1L to numberOfRound).foldLeft(Seq.empty[Phase]) { (phases, _) =>
      val previousPhaseNumber = phases.lastOption.map(_.rate.number).getOrElse(0)
      val nextPhase           = Phase(stepDuration, Rate(previousPhaseNumber + stepNumber, 1.second))
      phases :+ nextPhase
    }

    ScenarioSettings
      .empty(keySerializer, valueSerializer)
      .withBootstrapServers(bootstrapServer)
      .withTopic(topic)
      .withPhases(phases)
  }

}
