package com.github.stormgen.scenario

import cats.effect._
import com.github.stormgen.generator.Gen
import com.github.stormgen.metrics.{MetricsServer, MetricsStore}
import fs2.Stream
import fs2.kafka.ProducerSettings

import java.time.Instant
import scala.concurrent.duration.DurationLong

class Scenario[K, V](settings: ScenarioSettings[K, V])(implicit kGen: Gen[K], vGen: Gen[V]) {

  private val producerSettings = ProducerSettings[IO, K, V](settings.keySerializer, settings.valueSerializer).withBootstrapServers(settings.bootstrapServers)

  private val metricsStore = new MetricsStore

  def run: IO[Unit] = {
    val totalDuration = settings.phases.foldLeft(0L) { (acc, ele) => acc + ele.duration.toSeconds}
    val metricsServer: IO[Unit] = MetricsServer(metricsStore).useForever.timeoutTo(totalDuration.seconds, IO.println("stopped metrics server"))

    val stream: IO[Unit] = Stream.iterable(settings.phases).evalMap { phase =>
      MessageGenerator(settings.topic, phase)(kGen, vGen)
        .evalTap(msgs => IO.println(s"sending ${msgs.size} messages") >> metricsStore.updateSentMessage(Instant.now.toEpochMilli, msgs.size))
        .through(MessageSender(settings.topic, producerSettings)).compile.drain
    }.compile.drain

    (for {
      fiberMetricsServer <- metricsServer.start
      fiberStream <- stream.start
      _ <- fiberMetricsServer.join
      _ <- fiberStream.join
    } yield IO.unit) >> metricsStore.showReport
  }
}
