package com.github.stormgen.scenario

import scala.collection.immutable.Queue

import com.github.stormgen.scenario.JobExecutor.Init
import com.github.stormgen.scenario.JobExecutor.JobExecutorEvent
import com.github.stormgen.scenario.Scenario.ScenarioEvent
import com.github.stormgen.scenario.Scenario.ScenarioFinished
import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object JobQueue {
  sealed trait JobQueueEvent
  case class RunJob(step: Step) extends JobQueueEvent
  case object NextStep extends JobQueueEvent

  def apply(
      steps: Queue[Step],
      jobExecutorRef: ActorRef[JobExecutorEvent],
      scenarioRef: ActorRef[ScenarioEvent]
  ): Behavior[JobQueueEvent] = state(steps, jobExecutorRef, scenarioRef)

  private def state(
      steps: Queue[Step],
      jobExecutorRef: ActorRef[JobExecutorEvent],
      scenarioRef: ActorRef[ScenarioEvent]
  ): Behavior[JobQueueEvent] = Behaviors.setup { context =>
    val (step, queue) = steps.dequeue
    context.self ! RunJob(step)

    Behaviors.receiveMessage {
      case RunJob(step) =>
        jobExecutorRef ! Init(step.ratePerSecond, step.duration)
        Behaviors.same

      case NextStep =>
        if (queue.isEmpty) {
          scenarioRef ! ScenarioFinished
          Behaviors.stopped
        } else {
          state(queue, jobExecutorRef, scenarioRef)
        }
    }
  }
}
