package com.github.stormgen.metrics

import com.github.stormgen.metrics.Keys.Key
import munit.FunSuite

class MetricsStoreSuite extends FunSuite {

  test("MetricsStore.update stores and accumulates Data") {
    val store = new MetricsStore
    val key: Key = Keys.SentMessage
    val d1 = Datas.SentMessage(5)
    val d2 = Datas.SentMessage(7)
    store.update(key, d1)
    assertEquals(store.get(key), Some(d1))
    store.update(key, d2)
    assertEquals(store.get(key), Some(Datas.SentMessage(12)))
  }

  test("MetricsStore.get returns None for missing key") {
    val store = new MetricsStore
    assertEquals(store.get(Keys.SentMessage), None)
  }

  test("MetricsStore.apply creates a new store") {
    val store = MetricsStore()
    assert(store.isInstanceOf[MetricsStore])
  }
}


