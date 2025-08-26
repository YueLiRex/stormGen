package com.github.stormgen.reporter

import cats.effect.IO
import com.github.stormgen.metrics.{Keys, MetricsStore}

import scala.concurrent.duration.{DurationInt, FiniteDuration}

class ConsoleReporter(val metricsStore: MetricsStore) extends Reporter {
  override def report(duration: FiniteDuration): IO[Unit] =
    (IO.println(metricsStore.get(Keys.SentMessage)) >> IO.sleep(1.second)).foreverM.timeoutTo(duration, IO.unit)
}

object ConsoleReporter {
  def apply(metricsStore: MetricsStore) = new ConsoleReporter(metricsStore)
}
