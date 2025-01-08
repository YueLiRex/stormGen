package com.github.stormgen.collector

class MetricsCollector {
  private val producerSentMessageCounter = new Counter("Producer Sent Message")

  def updateProducerSentMessageCounter(number: Int): Int = producerSentMessageCounter + number

  def report: String =
    s"""
      |${producerSentMessageCounter.name}: ${producerSentMessageCounter.get}
      |""".stripMargin
}
