package com.github.stormgen.metrics

import scala.concurrent.duration.FiniteDuration

import cats.effect.IO

trait Reporter {
  def report(duration: FiniteDuration): IO[Unit]
}
