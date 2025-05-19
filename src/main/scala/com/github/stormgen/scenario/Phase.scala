package com.github.stormgen.scenario


import scala.concurrent.duration.FiniteDuration

case class Phase(duration: FiniteDuration, rate: Rate)

case class Rate(number: Int, perDuration: FiniteDuration)