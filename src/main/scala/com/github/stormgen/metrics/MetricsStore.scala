package com.github.stormgen.metrics

import cats.effect.IO
import cats.effect.std.MapRef
import com.github.stormgen.metrics.MetricsStore.SENT_MESSAGE

class MetricsStore {
  private val store = MapRef.ofSingleImmutableMap[IO, String, Double]()

  def updateSentMessage(number: Int): IO[Option[Double]] = store.flatMap{ map => map(SENT_MESSAGE).updateAndGet(_.map(d => d + number))}

  def exportMetrics: IO[Option[Double]] = store.flatMap { map =>
    val sentMessage = map(SENT_MESSAGE).get

    sentMessage
  }
}

object MetricsStore {
  val SENT_MESSAGE = "sent_message"
}
