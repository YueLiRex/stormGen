package com.github.stormgen.scenario

import cats.effect.IO
import com.github.stormgen.generator.Gen
import com.github.stormgen.generator.Generator
import fs2.Stream

object MessageGenerator {
  def apply[K, V](topic: String, phase: Phase)(implicit kGen: Gen[K], vGen: Gen[V]): Stream[IO, Seq[(K, V)]] = {
    val rate = phase.rate
    Stream(generateMessages(rate.number, topic)(kGen, vGen))
      .covary[IO]
      .repeat
      .meteredStartImmediately(rate.perDuration)
      .interruptAfter(phase.duration)
  }

  private def generateMessages[K, V](number: Int, topic: String)(implicit kGen: Gen[K], vGen: Gen[V]): Seq[(K, V)] =
    (1 to number).map { _ =>
      val key   = Generator.generate[K]
      val value = Generator.generate[V]
      (key, value)
    }
}
