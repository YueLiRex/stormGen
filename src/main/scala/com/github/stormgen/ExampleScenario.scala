package com.github.stormgen

import com.github.stormgen.scenario.{Scenario, ScenarioBuilder, ScenarioRunner, Step}
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}
import org.apache.pekko.actor.typed.ActorSystem

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt
import com.github.stormgen.generator.Generator._
import com.github.stormgen.kafka.KafkaConfig

object ExampleScenario extends ScenarioRunner {

  case class School(name: String, address: String)
  case class Test(name: String, age: Int, adult: Boolean, hobbies: Seq[String], historySchool: Seq[School])

  val testSerializer = new Serializer[Test] {
    override def serialize(topic: String, data: Test): Array[Byte] = data.toString.getBytes
  }

  val kafkaConfig = KafkaConfig("localhost:9092", "test", new StringSerializer, testSerializer)

  ScenarioBuilder[String, Test]()
    .name("test-scenario")
    .send(2, 3.seconds)
    .send(3, 5.seconds)
    .bootstrapServers("localhost:9092")
    .topic("test")
    .keySerializer(new StringSerializer)
    .valueSerializer(testSerializer)
    .build
    .run
}
