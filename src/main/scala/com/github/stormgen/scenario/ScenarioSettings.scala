package com.github.stormgen.scenario

import cats.effect._
import com.github.stormgen.generator.Gen
import fs2.kafka._

sealed abstract class ScenarioSettings[K, V] {
  def keySerializer: KeySerializer[IO, K]
  def valueSerializer: ValueSerializer[IO, V]

  def bootstrapServers: String
  def topic: String

  def phases: Seq[Phase]

  def compile(implicit kGen: Gen[K], vGen: Gen[V]): Scenario[K, V]

  def withBootstrapServers(servers: String): ScenarioSettings[K, V]
  def withTopic(topic: String): ScenarioSettings[K, V]
  def withPhase(phase: Phase): ScenarioSettings[K, V]
  def withPhases(phases: Seq[Phase]): ScenarioSettings[K, V]
}

object ScenarioSettings {
  final private[this] case class ScenarioSettingsImpl[K, V](
      phases: Seq[Phase],
      bootstrapServers: String,
      topic: String,
      keySerializer: KeySerializer[IO, K],
      valueSerializer: ValueSerializer[IO, V]
  ) extends ScenarioSettings[K, V] { self =>

    override def compile(implicit kGen: Gen[K], vGen: Gen[V]): Scenario[K, V] =
      new Scenario(self)

    override def withBootstrapServers(servers: String): ScenarioSettings[K, V] = this.copy(bootstrapServers = servers)

    override def withTopic(topic: String): ScenarioSettings[K, V] = this.copy(topic = topic)

    override def withPhase(phase: Phase): ScenarioSettings[K, V] = this.copy(phases = phases :+ phase)

    override def withPhases(phases: Seq[Phase]): ScenarioSettings[K, V] = this.copy(phases = this.phases :++ phases)
  }

  def create[K, V](
      keySerializer: KafkaSerializer[K],
      valueSerializer: KafkaSerializer[V]
  ): ScenarioSettings[K, V] =
    ScenarioSettingsImpl[K, V](
      Seq.empty,
      "null",
      "null",
      Serializer.delegate(keySerializer),
      Serializer.delegate(valueSerializer)
    )

}
