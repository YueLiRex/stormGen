package com.github.stormgen.scenario

import scala.collection.immutable.Queue
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

import com.github.stormgen.generator.Gen
import com.github.stormgen.kafka.EmptySerializer
import com.github.stormgen.kafka.KafkaConfig
import org.apache.kafka.common.serialization.Serializer
import org.apache.pekko.actor.typed.Behavior

case class ScenarioBuilder[K, V] private (
    name: String,
    steps: Queue[Step],
    bootstrapServers: String,
    topic: String,
    keySerializer: Serializer[K],
    valueSerializer: Serializer[V]
)(implicit keyGen: Gen[K], valueGen: Gen[V]) {
  def name(name: String): ScenarioBuilder[K, V] = copy(name = name)

  def send(ratePerSecond: Int, duration: FiniteDuration): ScenarioBuilder[K, V] = {
    val updatedSteps = steps.appended(Step(ratePerSecond, duration))
    copy(steps = updatedSteps)
  }

  def bootstrapServers(servers: String): ScenarioBuilder[K, V] =
    copy(bootstrapServers = servers)

  def topic(topic: String): ScenarioBuilder[K, V] = copy(topic = topic)

  def keySerializer(serializer: Serializer[K]): ScenarioBuilder[K, V] = copy(keySerializer = serializer)

  def valueSerializer(serializer: Serializer[V]): ScenarioBuilder[K, V] = copy(valueSerializer = serializer)

  def build: Behavior[Scenario.ScenarioEvent] = {
    val kafkaConfig = KafkaConfig(
      bootstrapServer = bootstrapServers,
      topic           = topic,
      keySerializer   = keySerializer,
      valueSerializer = valueSerializer
    )
    Scenario(steps = steps, kafkaConfig = kafkaConfig)
  }
}

object ScenarioBuilder {
  def apply[K, V](
      name: String                   = Random.alphanumeric.take(6).mkString,
      steps: Queue[Step]             = Queue.empty,
      bootstrapServers: String       = "",
      topics: String                 = "",
      keySerializer: Serializer[K]   = new EmptySerializer[K],
      valueSerializer: Serializer[V] = new EmptySerializer[V]
  )(implicit keyGen: Gen[K], valueGen: Gen[V]): ScenarioBuilder[K, V] = new ScenarioBuilder[K, V](
    name,
    steps,
    bootstrapServers,
    topics,
    keySerializer,
    valueSerializer
  )
}
