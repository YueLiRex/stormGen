package com.github.stormgen.scenario

import munit.FunSuite
import com.github.stormgen.generator.Gen
import fs2.kafka._
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}

import scala.concurrent.duration.DurationInt

class ScenarioSettingsSuite extends FunSuite {
  // Dummy serializers for testing
  val dummyKeySerializer: KafkaSerializer[String] = new StringSerializer
  val dummyValueSerializer: KafkaSerializer[Integer] = new IntegerSerializer

  test("ScenarioSettings.create initializes with defaults") {
    val settings = ScenarioSettings.create(dummyKeySerializer, dummyValueSerializer)
    assertEquals(settings.bootstrapServers, "null")
    assertEquals(settings.topic, "null")
    assertEquals(settings.phases, Seq.empty)
  }

  test("withBootstrapServers updates bootstrapServers") {
    val settings = ScenarioSettings.create(dummyKeySerializer, dummyValueSerializer)
      .withBootstrapServers("localhost:9092")
    assertEquals(settings.bootstrapServers, "localhost:9092")
  }

  test("withTopic updates topic") {
    val settings = ScenarioSettings.create(dummyKeySerializer, dummyValueSerializer)
      .withTopic("test-topic")
    assertEquals(settings.topic, "test-topic")
  }

  test("withPhase appends a phase") {
    val phase = Phase(10.second, Rate(2, 1.second))
    val settings = ScenarioSettings.create(dummyKeySerializer, dummyValueSerializer)
      .withPhase(phase)
    assertEquals(settings.phases, Seq(phase))
  }

  test("withPhases appends multiple phases") {
    val phases = Seq(Phase(10.second, Rate(2, 1.second)), Phase(10.second, Rate(2, 1.second)))
    val settings = ScenarioSettings.create(dummyKeySerializer, dummyValueSerializer)
      .withPhases(phases)
    assertEquals(settings.phases, phases)
  }
}
