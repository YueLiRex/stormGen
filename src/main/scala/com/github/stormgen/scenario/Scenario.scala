package com.github.stormgen.scenario

import cats.effect._
import com.github.stormgen.generator.Gen
import fs2.Stream
import fs2.kafka.ProducerSettings

class Scenario[K, V](settings: ScenarioSettings[K, V])(implicit kGen: Gen[K], vGen: Gen[V]) {

  private val producerSettings = ProducerSettings[IO, K, V](settings.keySerializer, settings.valueSerializer).withBootstrapServers(settings.bootstrapServers)

  def run: IO[Unit] = {
    Stream.iterable(settings.phases).evalMap { phase =>
      MessageGenerator(settings.topic, phase)(kGen, vGen).through(MessageSender(settings.topic, producerSettings)).compile.drain
    }.compile.drain
  }
}
