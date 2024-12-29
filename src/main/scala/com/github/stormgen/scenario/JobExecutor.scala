package com.github.stormgen.scenario

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

import com.github.stormgen.scenario.Scenario.JobFinished
import com.github.stormgen.scenario.Scenario.ScenarioEvent
import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object JobExecutor {
  sealed trait JobExecutorEvent
  case class Init(number: Int, duration: FiniteDuration) extends JobExecutorEvent
  case class Generate(number: Int) extends JobExecutorEvent
  case object Stop extends JobExecutorEvent

  def apply(scenarioRef: ActorRef[ScenarioEvent]): Behavior[JobExecutorEvent] = Behaviors.setup { context =>
    Behaviors.withTimers { timer =>
      Behaviors.receiveMessage {
        case Init(number, duration) =>
          timer.startTimerAtFixedRate("Generate", Generate(number), 1.seconds)
          timer.startSingleTimer("Stop", Stop, duration)
          Behaviors.same

        case Generate(number) =>
          println(s"Generate $number of message")
          Behaviors.same

        case Stop =>
          timer.cancelAll()
          scenarioRef ! JobFinished
          Behaviors.same
      }
    }
  }
}
