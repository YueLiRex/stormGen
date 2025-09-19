package com.github.stormgen.scenario

import cats.effect.Async
import fs2.Chunk
import fs2.Pipe
import fs2.kafka.KafkaProducer
import fs2.kafka.ProducerRecord
import fs2.kafka.ProducerResult
import fs2.kafka.ProducerSettings

object MessageSender {
  def apply[F[_]: Async, K, V](
      topic: String,
      producerSettings: ProducerSettings[F, K, V]
  ): Pipe[F, Seq[(K, V)], ProducerResult[K, V]] =
    inputStream =>
      inputStream
        .map { messages =>
          val records = messages.map { case (key, value) =>
            ProducerRecord(topic, key, value)
          }
          Chunk.from(records)
        }
        .through(KafkaProducer.pipe(producerSettings))
}
