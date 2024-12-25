package com.github.stormgen.scenario

import com.github.stormgen.scenario.Scenario.{JobFinished, ScenarioEvent}
import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import org.apache.pekko.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object JobExecutor {
  sealed trait JobExecutorEvent
  case class Init(number: Int, duration: FiniteDuration) extends JobExecutorEvent
  case class Generate(number: Int) extends JobExecutorEvent
  case object Stop extends JobExecutorEvent

  def apply(scenarioRef: ActorRef[ScenarioEvent]): Behavior[JobExecutorEvent] = Behaviors.withTimers { timer =>
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
