package com.github.stormgen.metrics

import cats.effect.IO
import cats.effect.std.MapRef

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters._

class MetricsStore {
  private val store = new ConcurrentHashMap[Long, Int]()

  def updateSentMessage(timestamp: Long, number: Int): IO[Unit] = IO(store.put(timestamp, number))

  def showReport: IO[Unit] = IO(store.asScala.foreach(println))
}
