package com.github.stormgen.scenario

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt

import com.github.stormgen.collector.MetricsCollector
import com.github.stormgen.generator.Gen
import com.github.stormgen.kafka.Committer
import com.github.stormgen.kafka.KafkaConfig
import com.github.stormgen.scenario.JobQueue.NextStep
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object Scenario {
  sealed trait ScenarioEvent

  case object JobFinished extends ScenarioEvent

  case object ScenarioFinished extends ScenarioEvent

  case class UpdateSentCounter(number: Int) extends ScenarioEvent

  private case object ReportMetrics extends ScenarioEvent

  def apply[K, V](
      steps: Queue[Step],
      kafkaConfig: KafkaConfig[K, V]
  )(implicit keyGen: Gen[K], valueGen: Gen[V]): Behavior[ScenarioEvent] =
    Behaviors.setup { context =>
      Behaviors.withTimers { timer =>
        timer.startTimerAtFixedRate(ReportMetrics, 3.seconds)

        val metricsCollector = new MetricsCollector

        val committerRef   = context.spawn(Committer(kafkaConfig), "committer")
        val jobExecutorRef = context.spawn(JobExecutor[K, V](context.self, committerRef), "job-executor")
        val jobQueueRef =
          context.spawn(JobQueue(steps = steps, jobExecutorRef = jobExecutorRef, scenarioRef = context.self), "job-queue")

        Behaviors.receiveMessage {
          case ReportMetrics =>
            println(metricsCollector.report)
            Behaviors.same

          case UpdateSentCounter(number) =>
            metricsCollector.updateProducerSentMessageCounter(number)
            Behaviors.same

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
}
