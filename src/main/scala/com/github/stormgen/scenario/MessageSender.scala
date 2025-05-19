package com.github.stormgen.scenario

import cats.effect.IO
import fs2.Chunk
import fs2.Pipe
import fs2.kafka.KafkaProducer
import fs2.kafka.ProducerRecord
import fs2.kafka.ProducerResult
import fs2.kafka.ProducerSettings

object MessageSender {
  def apply[K, V](topic: String, producerSettings: ProducerSettings[IO, K, V]): Pipe[IO, Seq[(K, V)], ProducerResult[K, V]] =
    inputStream => inputStream.map { messages =>
      val records = messages.map { case (key, value) =>
        ProducerRecord(topic, key, value)
      }
      Chunk.from(records)
    }.through(KafkaProducer.pipe(producerSettings))
}
