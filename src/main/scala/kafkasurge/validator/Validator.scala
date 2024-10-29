package kafkasurge.validator

trait Validator[V] {
  def validate(message: V): Boolean
}
