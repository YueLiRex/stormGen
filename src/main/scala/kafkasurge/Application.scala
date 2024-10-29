package kafkasurge

import kafkasurge.controller.Controller
import kafkasurge.generator.Generator
import kafkasurge.metrics.MetricsCollector
import kafkasurge.sender.KafkaSender

class Application[K, V](
                         kafkaSender: KafkaSender[K, V],
                         messageGenerator: Generator[V],
                         controller: Controller,
                         metricsCollector: MetricsCollector
                 ) {

  def start() = {
    val key: K = ???
    val message = messageGenerator.generate()
    kafkaSender.send(key, message)
    metricsCollector.collect()
  }
}
