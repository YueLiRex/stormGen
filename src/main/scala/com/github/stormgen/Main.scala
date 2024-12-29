package com.github.stormgen

import com.github.stormgen.scenario.{ Scenario, Step }
import org.apache.kafka.common.serialization.{ Serializer, StringSerializer }
import org.apache.pekko.actor.typed.ActorSystem

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt
import com.github.stormgen.generator.Generator._
import com.github.stormgen.kafka.KafkaConfig

object Main extends App {

  case class School(name: String, address: String)
  case class Test(name: String, age: Int, adult: Boolean, hobbies: Seq[String], historySchool: Seq[School])

  val testSerializer = new Serializer[Test] {
    override def serialize(topic: String, data: Test): Array[Byte] = data.toString.getBytes
  }

  val kafkaConfig = KafkaConfig("localhost:9092", "test", new StringSerializer, testSerializer)

  ActorSystem(Scenario[String, Test](Queue(Step(2, 3.seconds), Step(3, 5.seconds)), kafkaConfig), "scenario")
}
