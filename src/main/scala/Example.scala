import cats.effect._
import com.github.stormgen.generator.Generator._
import com.github.stormgen.scenario.{BasicBehaviour, Finish, Job, Kafka, NoBroker, Scenario, Settings}
import io.circe.generic.auto._
import io.circe.syntax._
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object Example extends IOApp {

  case class Weather(lat: Double, lon: Double, current: Current)
  case class Current(dt: Long, temp: Double, humidity: Int, clouds: Int, windSpeed: Double)

  val weatherSerializer = new Serializer[Weather] {
    override def serialize(topic: String, data: Weather): Array[Byte] = data.asJson.toString().getBytes
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val scenario = new Scenario[String, Weather](
      Settings(
        name = "test",
        brokerConfig = Kafka(
          bootstrapServers = List("localhost:9092"),
          topic = "test_topic",
          compressionType = "gzip",
          keySerializer = new StringSerializer,
          valueSerializer = weatherSerializer
        ),
        behaviours = List(
          BasicBehaviour(5.seconds, 30, 1.seconds),
          BasicBehaviour(10.seconds, 50, 1.seconds),
          BasicBehaviour(3.seconds, 100, 1.seconds)
        )
      )
    )

    scenario.run.map(_ => ExitCode.Success)
  }
}