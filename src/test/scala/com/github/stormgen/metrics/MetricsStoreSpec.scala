package com.github.stormgen.metrics

import cats.effect.IO
import cats.effect.std.Queue
import cats.effect.unsafe.implicits.global
import com.github.stormgen.metrics.Datas.Data
import com.github.stormgen.metrics.Keys.{Key, SentMessage}
import munit.FunSuite

class MetricsStoreSpec extends FunSuite {
  test("MetricsStore") {
    val metricsStore = MetricsStore()
    metricsStore.update(Keys.SentMessage, Datas.SentMessage(3))

    println(metricsStore.showReport)
  }
}
