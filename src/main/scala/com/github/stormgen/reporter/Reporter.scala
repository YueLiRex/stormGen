package com.github.stormgen.reporter

import cats.effect.IO
import com.github.stormgen.metrics.MetricsStore

import scala.concurrent.duration.FiniteDuration

trait Reporter {
  def metricsStore: MetricsStore
  def report(duration: FiniteDuration): IO[Unit]
}
