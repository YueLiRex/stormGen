package com.github.stormgen.scenario

import scala.collection.immutable.Queue
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

import com.github.stormgen.kafka.EmptySerializer
import org.apache.kafka.common.serialization.Serializer

case class ScenarioBuilder[K, V] private(
                                    name: String = Random.alphanumeric.take(6).mkString,
                                    steps: Queue[Step] = Queue.empty,
                                    bootstrapServers: String = "",
                                    topics: String = "",
                                    keySerializer: Serializer[K] = new EmptySerializer[K],
                                    valueSerializer: Serializer[V] = new EmptySerializer[V]
                                  ) {
  def name(name: String): ScenarioBuilder[K, V] = copy(name = name)

  def send(ratePerSecond: Int, duration: FiniteDuration): ScenarioBuilder[K, V] = {
    val updatedSteps = steps.appended(Step(ratePerSecond, duration))
    copy(steps = updatedSteps)
  }

  def bootstrapServers(servers: String): ScenarioBuilder[K, V] =
    copy(bootstrapServers = servers)

  def topics(topics: String): ScenarioBuilder[K, V] = copy(topics = topics)

  def keySerializer(serializer: Serializer[K]): ScenarioBuilder[K, V] = copy(keySerializer = serializer)

  def valueSerializer(serializer: Serializer[V]): ScenarioBuilder[K, V] = copy(valueSerializer = serializer)
}
