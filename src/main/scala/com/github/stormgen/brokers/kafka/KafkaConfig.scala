package com.github.stormgen.scenario

import org.apache.kafka.common.serialization.Serializer

sealed trait BrokerConfig

case object EmptyConfig extends BrokerConfig

case class KafkaConfig[K, V](
                              bootstrapServer: List[String],
                              topic: String,
                              keySerializer: Serializer[K],
                              valueSerializer: Serializer[V]) extends BrokerConfig
