package com.github.stormgen

import scala.concurrent.duration.DurationInt

import com.github.stormgen.generator.Generator._
import com.github.stormgen.kafka.KafkaConfig
import com.github.stormgen.scenario.ScenarioBuilder
import com.github.stormgen.scenario.ScenarioRunner
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringSerializer

object ExampleScenario extends ScenarioRunner {

  case class School(name: String, address: String)
  case class Test(name: String, age: Int, adult: Boolean, hobbies: Seq[String], historySchool: Seq[School])

  val testSerializer: Serializer[Test] = new Serializer[Test] {
    override def serialize(topic: String, data: Test): Array[Byte] = data.toString.getBytes
  }

  ScenarioBuilder[String, Test]()
    .name("test-scenario")
    .bootstrapServers("localhost:9092")
    .topic("test")
    .keySerializer(new StringSerializer)
    .valueSerializer(testSerializer)
    .send(2, 3.seconds)
    .send(3, 5.seconds)
    .send(5, 10.seconds)
    .build
    .run
}
