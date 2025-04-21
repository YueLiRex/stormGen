package com.github.stormgen.scenario

import cats.effect.IO
import cats.effect.std.Queue
import com.github.stormgen.generator.Gen

import scala.concurrent.duration.DurationInt

class Scenario[K, V](settings: Settings[K, V])(implicit kGen: Gen[K], vGen: Gen[V]) {

  private def initStages(stageQueue: Queue[IO, Behaviour]): IO[Unit] = settings.behaviours.foldLeft(IO.unit) { (acc, stage) => acc >> stageQueue.offer(stage) }

  private def pushJobToQueue(behaviourQueue: Queue[IO, Behaviour], jobQueue: Queue[IO, Job]): IO[Unit] =
    behaviourQueue.tryTake.flatMap {
      case Some(stage) =>
        (jobQueue.offer(GenerateMessage(stage.rateNumber)) >> IO.println(s"Generate ${stage.rateNumber} job enqueued") >> IO.sleep(stage.rateDuration)).foreverM.timeoutTo(stage.duration, pushJobToQueue(behaviourQueue, jobQueue))

      case None => jobQueue.offer(Finish) >> IO.unit
    }.onCancel(IO.println("Executed all stages. Exist program"))

  def run: IO[Unit] = {
    for {
      behaviourQueue <- Queue.bounded[IO, Behaviour](settings.behaviours.size)
      _ <- initStages(behaviourQueue)
      jobQueue <- Queue.unbounded[IO, Job]
      stageFiber <- pushJobToQueue(behaviourQueue, jobQueue).start
      producerFiber <- MessageProducer[K, V](settings.brokerConfig, jobQueue).start
      _ <- stageFiber.join
      _ <- producerFiber.join
    } yield ()
  }
}
