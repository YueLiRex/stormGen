package com.github.stormgen.scenario

import org.apache.kafka.common.serialization.Serializer

sealed trait BrokerConfig

case object NoBroker extends BrokerConfig

case class Kafka[K, V](
                        bootstrapServers: List[String],
                        topic: String,
                        compressionType: String,
                        keySerializer: Serializer[K],
                        valueSerializer: Serializer[V]
                      ) extends BrokerConfig
