package com.github.stormgen.brokers

class BrokerSelector {
  def select(name: String): Broker = name match {
    case "KAFKA" => Kafka(bootstrapServers = Seq.empty, topics = Seq.empty)
  }
}
