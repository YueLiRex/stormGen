package com.github.stormgen.brokers

sealed trait Broker

case object NoBroker extends Broker

case class Kafka(bootstrapServers: Seq[String], topics: Seq[String]) extends Broker
