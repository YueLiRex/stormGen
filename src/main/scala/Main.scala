import cats.effect._
import cats.effect.kernel.Deferred
import cats.effect.std.Queue
import com.github.stormgen.scenario.{Finish, Job, NoBroker, Scenario, Settings}

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val scenario = new Scenario[String, String](Settings(name = "test", brokerConfig = NoBroker, behaviours = List.empty))

    scenario.run.map(_ => ExitCode.Success)
  }
}