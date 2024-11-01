package stormgen.validator

trait Validator[V] {
  def validate(message: V): Boolean
}
