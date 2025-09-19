package com.github.stormgen.scenario

import scala.concurrent.duration.DurationInt

import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants.COMPRESSION
import com.github.stormgen.scenario.Config.BrokerConfig
import com.github.stormgen.scenario.Config.ScenarioConfig
import com.github.stormgen.scenario.Config.SerdeConfig
import com.typesafe.config.ConfigFactory
import munit.FunSuite
import software.amazon.awssdk.services.glue.model.Compatibility

class ConfigSuite extends FunSuite {
  test("Config.apply") {
    val config = Config(ConfigFactory.load())

    val expectedConfig = ScenarioConfig(
      name = "test-scenario",
      phaseList = Seq(
        Phase(
          duration = 10.seconds,
          rate     = Rate(number = 10, perDuration = 1.second)
        ),
        Phase(
          duration = 3.seconds,
          rate     = Rate(number = 5, perDuration = 1.second)
        )
      ),
      brokerConfig = BrokerConfig(
        bootstrapServers = "test-server1, test-server2",
        topic            = "test-topic1",
        schemaRegion     = "EU-CENTER",
        dataFormat       = "JSON"
      ),
      serdeConfig = SerdeConfig(
        schemaAutoRegistrationSettings = false,
        schemaName                     = "test-schema-name",
        registryName                   = "MyDomainName",
        cacheTtlMilli                  = 86400000,
        cacheSize                      = 100,
        compatibilitySetting           = Compatibility.FULL,
        description                    = Some("test-description"),
        compressionType                = COMPRESSION.NONE
      )
    )

    assertEquals(config, expectedConfig)
  }
}
