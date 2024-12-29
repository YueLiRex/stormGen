package com.github.stormgen.scenario

import com.github.stormgen.generator.{ Gen, Generator }
import com.github.stormgen.kafka.Committer.{ CommitterEvent, Submit }
import com.github.stormgen.kafka.KafkaConfig

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration
import com.github.stormgen.scenario.Scenario.JobFinished
import com.github.stormgen.scenario.Scenario.ScenarioEvent
import org.apache.kafka.clients.producer.{ KafkaProducer, Producer, ProducerRecord }
import org.apache.kafka.common.serialization.Serializer
import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

import java.util.Properties

object JobExecutor {
  sealed trait JobExecutorEvent
  case class Init(number: Int, duration: FiniteDuration) extends JobExecutorEvent
  case class Generate(number: Int) extends JobExecutorEvent
  case object Stop extends JobExecutorEvent

  def apply[K, V](
      scenarioRef: ActorRef[ScenarioEvent],
      committerRef: ActorRef[CommitterEvent[K, V]]
  )(implicit keyGen: Gen[K], valueGen: Gen[V]): Behavior[JobExecutorEvent] = Behaviors.withTimers { timer =>
    Behaviors.receiveMessage {
      case Init(number, duration) =>
        timer.startTimerAtFixedRate("Generate", Generate(number), 1.seconds)
        timer.startSingleTimer("Stop", Stop, duration)
        Behaviors.same

      case Generate(number) =>
        val messages = (1 to number).map { _ =>
          val key   = Generator.generate[K]
          val value = Generator.generate[V]
          (key, value)
        }
        committerRef ! Submit(messages)
        Behaviors.same

      case Stop =>
        timer.cancelAll()
        scenarioRef ! JobFinished
        Behaviors.same
    }
  }
}
