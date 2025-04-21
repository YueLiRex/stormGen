package com.github.stormgen.scenario

import scala.concurrent.duration.FiniteDuration

sealed trait Behaviour {
  def duration: FiniteDuration

  def rateNumber: Int

  def rateDuration: FiniteDuration
}

case class BasicBehaviour(duration: FiniteDuration, rateNumber: Int, rateDuration: FiniteDuration) extends Behaviour

sealed trait Job

case class GenerateMessage(number: Int) extends Job

case object Finish extends Job
