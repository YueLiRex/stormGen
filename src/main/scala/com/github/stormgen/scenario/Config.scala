package com.github.stormgen.scenario

import scala.concurrent.duration._
import scala.jdk.CollectionConverters._

import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants
import software.amazon.awssdk.services.glue.model.Compatibility

object Config {
  case class BrokerConfig(
      bootstrapServers: String,
      topic: String,
      schemaRegion: String,
      dataFormat: String
  )
  case class SerdeConfig(
      schemaAutoRegistrationSettings: Boolean,
      schemaName: String,
      registryName: String,
      cacheTtlMilli: Long,
      cacheSize: Int,
      compatibilitySetting: Compatibility,
      description: Option[String],
      compressionType: AWSSchemaRegistryConstants.COMPRESSION
  )

  case class ScenarioConfig(
      name: String,
      phaseList: Seq[Phase],
      brokerConfig: BrokerConfig,
      serdeConfig: SerdeConfig
  )

  def apply(config: com.typesafe.config.Config): ScenarioConfig = {
    val name = config.getString("name")
    val phaseList = config
      .getConfigList("phase-list")
      .listIterator()
      .asScala
      .map { phaseConfig =>
        val duration = phaseConfig.getDuration("duration").toMillis.milli
        val rate     = Rate(phaseConfig.getInt("rate.number"), phaseConfig.getDuration("rate.perDuration").toMillis.milli)
        Phase(duration, rate)
      }
      .toSeq

    val bootstrapServers = config.getString("broker.bootstrap-servers")
    val topic            = config.getString("broker.topic")
    val schemaRegion     = config.getString("broker.schema-region")
    val dataFormat       = config.getString("broker.data-format")

    val schemaAutoRegistrationSettings = config.getBoolean("serde.auto-registration-settings")
    val schemaName                     = config.getString("serde.schema-name")
    val registryName                   = config.getString("serde.registry-name")
    val cacheTtlMilli                  = config.getLong("serde.cache-ttl-milli")
    val cacheSize                      = config.getInt("serde.cache-size")
    val compatibilitySetting           = Compatibility.fromValue(config.getString("serde.compatibility-setting").toUpperCase)
    val description = if (config.getString("serde.description").isEmpty) None else Some(config.getString("serde.description"))
    val compressionType = config.getString("serde.compression-type") match {
      case "NONE" => AWSSchemaRegistryConstants.COMPRESSION.NONE
      case "ZLIB" => AWSSchemaRegistryConstants.COMPRESSION.ZLIB
    }

    val brokerConfig = BrokerConfig(bootstrapServers, topic, schemaRegion, dataFormat)
    val serdeConfig = SerdeConfig(
      schemaAutoRegistrationSettings = schemaAutoRegistrationSettings,
      schemaName                     = schemaName,
      registryName                   = registryName,
      cacheTtlMilli                  = cacheTtlMilli,
      cacheSize                      = cacheSize,
      compatibilitySetting           = compatibilitySetting,
      description                    = description,
      compressionType                = compressionType
    )

    ScenarioConfig(
      name         = name,
      phaseList    = phaseList,
      brokerConfig = brokerConfig,
      serdeConfig  = serdeConfig
    )
  }
}
