package com.github.stormgen.scenario

import cats.effect.{IO, Resource}
import cats.effect.std.Queue
import com.github.stormgen.generator.Gen
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

object MessageProducer {
  def apply[K, V](topic: String, jobQueue: Queue[IO, Job])(implicit kGen: Gen[K], vGen: Gen[V]) = {
    Resource.make(
      IO.println("creating kafka producer") >>IO {
        val props = new Properties()
        new KafkaProducer[K, V](props)
      }
    )(kafkaProducer => IO(kafkaProducer.close())).use { producer =>
      jobQueue.take.flatMap {
        case GenerateMessage(number) =>
          (1 to number).foreach { _ =>
            val key = kGen.getValue
            val value = vGen.getValue
            producer.send(new ProducerRecord[K, V](topic, key, value))
          }
          IO.println(s"Generating $number of message ${kGen.getValue} -> ${vGen.getValue}")
        case Finish =>
          IO.println(s"Get Finish job") >> IO.canceled
      }.foreverM
    }
  }
}
