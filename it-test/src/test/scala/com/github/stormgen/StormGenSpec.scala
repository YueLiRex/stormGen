package com.github.stormgen

import com.dimafeng.testcontainers.KafkaContainer
import com.dimafeng.testcontainers.munit.TestContainerForAll
import munit.FunSuite

class StormGenSpec extends FunSuite with TestContainerForAll {

  override val containerDef: KafkaContainer.Def = KafkaContainer.Def()

  test("kafka container should start") {
    val kafkaContainer = containerDef.createContainer()

    kafkaContainer.start()

    val bootstrapServer = kafkaContainer.bootstrapServers

    println(bootstrapServer)

    kafkaContainer.stop()

  }
}
