package com.github.stormgen.scenario

import cats.effect.Spawn
import cats.effect._
import com.github.stormgen.generator.Gen
import com.github.stormgen.generator.Generator
import fs2.Chunk
import fs2.Pipe
import fs2.Stream
import fs2.kafka._

class Scenario[F[_]: Async, K, V](settings: ScenarioSettings[F, K, V])(implicit kGen: Gen[K], vGen: Gen[V]) {

  private val producerSettings: ProducerSettings[F, K, V] =
    ProducerSettings[F, K, V](settings.keySerializer, settings.valueSerializer).withBootstrapServers(settings.bootstrapServers)

  private def stream: F[Unit] = Stream
    .iterable(settings.phases)
    .evalMap { phase =>
      Stream(generateMessages(phase.rate.number))
        .covary[F]
        .repeat
        .meteredStartImmediately(phase.rate.perDuration)
        .through(sendMessages)
        .interruptAfter(phase.duration)
        .compile
        .drain
    }
    .compile
    .drain

  private def generateMessages(number: Int): Seq[(K, V)] =
    (1 to number).map { _ =>
      val key   = Generator.generate[K](kGen)
      val value = Generator.generate[V](vGen)
      (key, value)
    }

  private def sendMessages: Pipe[F, Seq[(K, V)], ProducerResult[K, V]] = inputStream =>
    inputStream
      .map { messages =>
        val records = messages.map { case (key, value) =>
          ProducerRecord(settings.topic, key, value)
        }
        Chunk.from(records)
      }
      .through(KafkaProducer.pipe(producerSettings))

  def start: F[Fiber[F, Throwable, Unit]] = Spawn[F].start(stream)
}
