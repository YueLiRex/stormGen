package com.github.stormgen.scenario

import cats.effect._
import com.github.stormgen.generator.Gen
import fs2.kafka._

sealed abstract class ScenarioSettings[F[_], K, V] {
  def keySerializer: KeySerializer[F, K]
  def valueSerializer: ValueSerializer[F, V]

  def bootstrapServers: String
  def topic: String
  def phases: Seq[Phase]

  def compile(implicit kGen: Gen[K], vGen: Gen[V]): Scenario[F, K, V]

  def withBootstrapServers(servers: String): ScenarioSettings[F, K, V]
  def withTopic(topic: String): ScenarioSettings[F, K, V]
  def withPhase(phase: Phase): ScenarioSettings[F, K, V]
  def withPhases(phases: Seq[Phase]): ScenarioSettings[F, K, V]
}

object ScenarioSettings {
  final private[this] case class ScenarioSettingsImpl[F[_]: Async, K, V](
      phases: Seq[Phase],
      bootstrapServers: String,
      topic: String,
      keySerializer: KeySerializer[F, K],
      valueSerializer: ValueSerializer[F, V]
  ) extends ScenarioSettings[F, K, V] { self =>

    override def compile(implicit kGen: Gen[K], vGen: Gen[V]): Scenario[F, K, V] =
      new Scenario(self)

    override def withBootstrapServers(servers: String): ScenarioSettings[F, K, V] = this.copy(bootstrapServers = servers)

    override def withTopic(topic: String): ScenarioSettings[F, K, V] = this.copy(topic = topic)

    override def withPhase(phase: Phase): ScenarioSettings[F, K, V] = this.copy(phases = phases :+ phase)

    override def withPhases(phases: Seq[Phase]): ScenarioSettings[F, K, V] = this.copy(phases = this.phases :++ phases)
  }

  def empty[F[_]: Async, K, V](
      keySerializer: KafkaSerializer[K],
      valueSerializer: KafkaSerializer[V]
  ): ScenarioSettings[F, K, V] =
    ScenarioSettingsImpl[F, K, V](
      Seq.empty,
      "null",
      "null",
      Serializer.delegate(keySerializer),
      Serializer.delegate(valueSerializer)
    )
}
