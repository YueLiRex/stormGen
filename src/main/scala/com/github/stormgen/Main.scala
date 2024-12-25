package com.github.stormgen

import com.github.stormgen.scenario.Scenario._
import com.github.stormgen.scenario.{JobExecutor, Scenario, Step}
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem, Behavior}
import org.apache.pekko.actor.typed.scaladsl.Behaviors

import scala.collection.immutable.Queue
import scala.concurrent.duration.DurationInt

object Main extends App {

  case class School(name: String, address: String)
  case class Test(name: String, age: Int, adult: Boolean, hobbies: Seq[String], historySchool: Seq[School])

  ActorSystem(Scenario(Queue(Step(3, 3.seconds), Step(2, 5.seconds))), "root")
}
