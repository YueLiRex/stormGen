package com.github.stormgen.kafka

import java.util.Properties

import com.typesafe.config.Config
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serializer

case class KafkaConfig[K, V](
    bootstrapServer: String,
    topic: String,
    keySerializer: Serializer[K],
    valueSerializer: Serializer[V]
) {
  def toProperties: Properties = {
    val properties = new Properties()
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)

    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer.getClass)
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer.getClass)

    properties
  }
}

object KafkaConfig {
  def apply[K, V](config: Config, keySerializer: Serializer[K], valueSerializer: Serializer[V]) = new KafkaConfig[K, V](
    bootstrapServer = config.getString("kafka.bootstrap-server"),
    topic           = config.getString("kafka.topic"),
    keySerializer   = keySerializer,
    valueSerializer = valueSerializer
  )
}
