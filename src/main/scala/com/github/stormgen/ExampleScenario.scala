package com.github.stormgen

import cats.effect.{ExitCode, IO}
import com.github.stormgen.scenario.{Scenario, ScenarioApp}
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}

import scala.concurrent.duration.DurationInt


object ExampleScenario extends ScenarioApp {
  case class School(name: String, address: String)
  case class Test(name: String, age: Int, adult: Boolean, hobbies: Seq[String], historySchool: Seq[School])

  val testSerializer: Serializer[Test] = new Serializer[Test] {
    override def serialize(topic: String, data: Test): Array[Byte] = data.toString.getBytes
  }

  override def run(args: List[String]): IO[ExitCode] = {
    scenario
      .name("scenario with cats effect")
      .withBroker[String, Test](
        bootstrapServers = List("localhost:9092"),
        topic = "test-topic",
        keySerializer = new StringSerializer,
        valueSerializer = testSerializer
      )
      .withNumberOfProducer(1)
      .step(10, 3.seconds)
      .step(3, 5.seconds)
      .start().map(_ => ExitCode.Success)

  }
}
