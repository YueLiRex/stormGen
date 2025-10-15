package example

import scala.concurrent.duration.DurationInt
import cats.effect.ExitCode
import cats.effect.IO
import com.github.stormgen.ScenarioRunner
import com.github.stormgen.scenario.Phase
import com.github.stormgen.scenario.Rate
import com.github.stormgen.scenario.ScenarioSettings
import org.apache.kafka.common.serialization.StringSerializer

object Application extends ScenarioRunner {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      fiber <- ScenarioSettings
        .empty[IO, String, String](keySerializer = new StringSerializer, valueSerializer = new StringSerializer)
        .withBootstrapServers("localhost:9092")
        .withTopic("test")
        .withPhase(Phase(10.seconds, Rate(10, 1.second)))
        .withPhases(Seq(Phase(3.second, Rate(3, 1.second))))
        .run
      _ <- fiber.join
    } yield ExitCode.Success
}

