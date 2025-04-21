package com.github.stormgen.scenario

import org.apache.kafka.common.serialization.Serializer

import scala.util.Random

case class Settings[K, V](
    name: String,
    brokerConfig: Kafka[K, V],
    behaviours: List[Behaviour]) {

  def name(name: String): Settings[K, V] = this.copy(name = name)

  def configBroker(brokerConfig: Kafka[K, V]): Settings[K, V] = this.copy(brokerConfig = brokerConfig)

  def addBehaviour(behaviour: Behaviour): Settings[K, V] = this.copy(behaviours = this.behaviours :+ behaviour)
}
