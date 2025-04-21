package com.github.stormgen.scenario

import cats.effect.{IO, Resource}
import cats.effect.std.Queue
import com.github.stormgen.generator.Gen
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties

object MessageProducer {
  def apply[K, V](brokerConfig: Kafka[K, V], jobQueue: Queue[IO, Job])(implicit kGen: Gen[K], vGen: Gen[V]) = {
    Resource.make(
      IO.println("creating kafka producer") >>IO {
        val props = new Properties()
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerConfig.bootstrapServers.mkString(","))
//        props.setProperty(ProducerConfig.ACKS_CONFIG, "1")
//        props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20")
//        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "32")
        props.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, brokerConfig.compressionType)
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, brokerConfig.keySerializer.getClass.getName)
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, brokerConfig.valueSerializer.getClass.getName)
        new KafkaProducer[K, V](props)
      }
    )(kafkaProducer => IO(kafkaProducer.close())).use { producer =>
      jobQueue.take.flatMap {
        case GenerateMessage(number) =>
          IO {
            (1 to number).foreach { _ =>
              val key = kGen.getValue
              val value = vGen.getValue
              producer.send(new ProducerRecord[K, V](brokerConfig.topic, key, value))
            }
          }
        case Finish =>
          IO.println(s"Get Finish job") >> IO.canceled
      }.foreverM
    }
  }
}
