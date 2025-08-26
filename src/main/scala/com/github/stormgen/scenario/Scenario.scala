package com.github.stormgen.scenario

import cats.effect._
import cats.effect.std.Queue
import com.github.stormgen.generator.Gen
import com.github.stormgen.metrics.Datas.{Data, SentMessage}
import com.github.stormgen.metrics.Keys.Key
import com.github.stormgen.metrics.{Datas, Keys, MetricsServer, MetricsStore}
import com.github.stormgen.reporter.ConsoleReporter
import fs2.Stream
import fs2.kafka.ProducerSettings

import java.time.Instant
import scala.concurrent.duration.DurationLong

class Scenario[K, V](settings: ScenarioSettings[K, V])(implicit kGen: Gen[K], vGen: Gen[V]) {

  private val producerSettings = ProducerSettings[IO, K, V](settings.keySerializer, settings.valueSerializer).withBootstrapServers(settings.bootstrapServers)

  def run: IO[Unit] = {
//    val totalDuration = settings.phases.foldLeft(0L) { (acc, ele) => acc + ele.duration.toSeconds}

    val metricsStore = MetricsStore()
//    val consoleReporter = ConsoleReporter(metricsStore)

    val stream: IO[Unit] = Stream.iterable(settings.phases).evalMap { phase =>
      MessageGenerator(settings.topic, phase)(kGen, vGen)
        .evalTap(msgs => IO(metricsStore.update(Keys.SentMessage, Datas.SentMessage(msgs.size))) >> IO.println(metricsStore.get(Keys.SentMessage)))
        .through(MessageSender(settings.topic, producerSettings)).compile.drain
    }.compile.drain

    for {
//      fiberMetricsServer <- metricsServer.start
      fiberStream <- stream.start
//      reporter <- consoleReporter.report(totalDuration.seconds).start
//      _ <- fiberMetricsServer.join
      _ <- fiberStream.join
//      _ <- reporter.join
    } yield IO.unit
  }
}
