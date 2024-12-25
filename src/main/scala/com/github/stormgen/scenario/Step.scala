package com.github.stormgen.scenario

import scala.concurrent.duration.FiniteDuration

case class Step(ratePerSecond: Int, duration: FiniteDuration)
