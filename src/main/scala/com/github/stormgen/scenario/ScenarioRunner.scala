package com.github.stormgen.scenario

import cats.effect.IO
import org.apache.kafka.clients.producer.KafkaProducer

import java.util.Properties
import java.util.concurrent.Executors
import scala.concurrent.duration.DurationInt

abstract class ScenarioRunner[K, V] { self: Scenario =>

  val props = new Properties()
  val producer = new KafkaProducer[K, V](props)

  val scheduler = Executors.newScheduledThreadPool(1)

  def start() = {
    scheduler.scheduleAtFixedRate()
  }
}
