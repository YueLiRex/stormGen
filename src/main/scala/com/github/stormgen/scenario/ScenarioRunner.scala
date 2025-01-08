package com.github.stormgen.scenario

import com.github.stormgen.scenario.Scenario.ScenarioEvent
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.Behavior

trait ScenarioRunner extends App {
  implicit class Runner(scenarioBehavior: Behavior[ScenarioEvent]) {
    def run: ActorSystem[ScenarioEvent] = ActorSystem(scenarioBehavior, "scenario-runner")
  }
}
