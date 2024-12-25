package com.github.stormgen.scenario

import com.github.stormgen.scenario.JobQueue.NextStep
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

import scala.collection.immutable.Queue

object Scenario {
  sealed trait ScenarioEvent
  case object JobFinished extends ScenarioEvent
  case object ScenarioFinished extends ScenarioEvent

  def apply(steps: Queue[Step]): Behavior[ScenarioEvent] = behavior(steps)

  private def behavior(steps: Queue[Step]): Behavior[ScenarioEvent] = Behaviors.setup { context =>
    val jobExecutorRef = context.spawn(JobExecutor(context.self), "job-executor")
    val jobQueueRef = context.spawn(JobQueue(steps = steps, jobExecutorRef = jobExecutorRef, scenarioRef = context.self), "job-queue")

    Behaviors.receiveMessage {
      case JobFinished =>
        jobQueueRef ! NextStep
        Behaviors.same

      case ScenarioFinished =>
        context.stop(jobQueueRef)
        context.stop(jobExecutorRef)
        Behaviors.stopped(() => println("scenario stopped"))
    }
  }

}


