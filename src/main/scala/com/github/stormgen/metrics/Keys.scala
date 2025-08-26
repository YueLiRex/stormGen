package com.github.stormgen.metrics

object Keys {
  sealed trait Key
  case object SentMessage extends Key
}
