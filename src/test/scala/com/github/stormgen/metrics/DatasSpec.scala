package com.github.stormgen.metrics

import com.github.stormgen.metrics.Datas.{Empty, SentMessage}
import munit.FunSuite

class DatasSpec extends FunSuite {
  test("Data type plus") {
    val result1 = SentMessage(1) + SentMessage(4)
    assertEquals(result1, SentMessage(5))

    val result2 = Empty + SentMessage(1)
    assertEquals(result2, SentMessage(1))

    val result3 = SentMessage(2) + Empty
    assertEquals(result3, SentMessage(2))
  }

  test("Metrics store update data") {

  }
}
