package com.github.stormgen.metrics

import com.github.stormgen.metrics.Datas.{Data, Empty}
import com.github.stormgen.metrics.Keys.Key

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters._


class MetricsStore {
  private val store = new ConcurrentHashMap[Key, Data]()

  def update(key: Key, data: Data): Data = {
    val newData = store.getOrDefault(key, Empty) + data
    store.put(key, newData)
  }

  def get(key: Key) = Option(store.get(key))
}

object MetricsStore {
  def apply() = new MetricsStore()
}
