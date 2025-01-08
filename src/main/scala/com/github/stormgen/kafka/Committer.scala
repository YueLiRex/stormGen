package com.github.stormgen.kafka

import com.github.stormgen.kafka.Committer.CommitterEvent
import com.github.stormgen.kafka.Committer.Submit
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.AbstractBehavior
import org.apache.pekko.actor.typed.scaladsl.ActorContext
import org.apache.pekko.actor.typed.scaladsl.Behaviors

// todo acks = all to ensure broker received message or acks = 0 to ignore the response for speed
class Committer[K, V] private (kafkaConfig: KafkaConfig[K, V])(implicit context: ActorContext[CommitterEvent[K, V]])
    extends AbstractBehavior[CommitterEvent[K, V]](context) {

  private val kafkaProducer = new KafkaProducer[K, V](kafkaConfig.toProperties)

  override def onMessage(msg: CommitterEvent[K, V]): Behavior[CommitterEvent[K, V]] = msg match {
    case Submit(messages) =>
      messages.foreach { case (key, value) =>
        kafkaProducer.send(new ProducerRecord(kafkaConfig.topic, key, value))
      }
      this
  }
}

object Committer {
  sealed trait CommitterEvent[K, V]
  case class Submit[K, V](messages: Seq[(K, V)]) extends CommitterEvent[K, V]

  def apply[K, V](kafkaConfig: KafkaConfig[K, V]): Behavior[CommitterEvent[K, V]] =
    Behaviors.setup { implicit context =>
      new Committer(kafkaConfig)(context)
    }
}
