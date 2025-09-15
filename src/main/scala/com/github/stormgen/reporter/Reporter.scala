package com.github.stormgen.reporter

import scala.concurrent.duration.FiniteDuration

import cats.effect.IO

trait Reporter {
  def report(duration: FiniteDuration): IO[Unit]
}
