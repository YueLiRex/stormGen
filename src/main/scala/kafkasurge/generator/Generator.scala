package kafkasurge.generator

import kafkasurge.generator.Generator.Config

trait Generator[V] {
  def generate(): V
}

object Generator {
  case class Config()
}
