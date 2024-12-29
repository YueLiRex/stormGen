package com.github.stormgen.scenario

import com.github.stormgen.generator.Gen
import com.github.stormgen.kafka.{ Committer, KafkaConfig }

import scala.collection.immutable.Queue
import com.github.stormgen.scenario.JobQueue.NextStep
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.kafka.common.serialization.Serializer

object Scenario {
  sealed trait ScenarioEvent

  case object JobFinished extends ScenarioEvent

  case object ScenarioFinished extends ScenarioEvent

  def apply[K, V](
      steps: Queue[Step],
      kafkaConfig: KafkaConfig[K, V]
  )(implicit keyGen: Gen[K], valueGen: Gen[V]): Behavior[ScenarioEvent] = Behaviors.setup { context =>
    val committerRef   = context.spawn(Committer(kafkaConfig), "committer")
    val jobExecutorRef = context.spawn(JobExecutor[K, V](context.self, committerRef), "job-executor")
    val jobQueueRef =
      context.spawn(JobQueue(steps = steps, jobExecutorRef = jobExecutorRef, scenarioRef = context.self), "job-queue")

    Behaviors.receiveMessage {
      case JobFinished =>
        jobQueueRef ! NextStep
        Behaviors.same

      case ScenarioFinished =>
        context.stop(jobQueueRef)
        context.stop(jobExecutorRef)
        context.stop(committerRef)
        Behaviors.stopped(() => println("scenario stopped"))
    }
  }

}
