package com.github.stormgen.kafka

import org.apache.kafka.common.serialization.Serializer

class EmptySerializer[T] extends Serializer[T] {
  override def serialize(topic: String, data: T): Array[Byte] = Array.empty
}
