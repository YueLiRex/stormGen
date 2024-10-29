package kafkasurge.sender

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import java.util.Properties
import java.util.concurrent.Future

class KafkaSender[K, V](props: Properties, topic: String) {
  private val producer = new KafkaProducer[K, V](props)

  def send(key: K, message: V): Future[RecordMetadata] = {
    val record = new ProducerRecord[K, V](topic, key, message)
    producer.send(record)
  }
}
