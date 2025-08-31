package com.github.stormgen.metrics

import com.github.stormgen.metrics.Datas.{Empty, SentMessage}
import munit.FunSuite

class DatasSuite extends FunSuite {
  import Datas._

  test("Empty + Empty is Empty") {
    assertEquals(Empty + Empty, Empty)
  }

  test("Empty + SentMessage returns SentMessage") {
    val sm = SentMessage(5)
    assertEquals(Empty + sm, sm)
  }

  test("SentMessage + Empty returns itself") {
    val sm = SentMessage(7)
    assertEquals(sm + Empty, sm)
  }

  test("SentMessage + SentMessage sums numbers") {
    val sm1 = SentMessage(3)
    val sm2 = SentMessage(4)
    assertEquals(sm1 + sm2, SentMessage(7))
  }

  test("Empty.show returns 'Empty'") {
    assertEquals(Empty.show, "Empty")
  }

  test("SentMessage.show returns correct string") {
    val sm = SentMessage(42)
    assertEquals(sm.show, "Sent 42 messages")
  }
}
