import sbt.*

object Dependencies {
  lazy val scalaV = "2.13.16"

  lazy val circeVersion          = "0.14.10"
  lazy val catsEffectVersion     = "3.6.0"
  lazy val http4sVersion         = "0.23.30"
  lazy val testcontainersVersion = "0.43.0"
  lazy val kafkaVersion          = "4.0.0"

  lazy val typeSafeConfig = "com.typesafe"     % "config"        % "1.4.4"
  lazy val kafkaClients39 = "org.apache.kafka" % "kafka-clients" % kafkaVersion
  lazy val shapeless      = "com.chuusai"     %% "shapeless"     % "2.3.13"

  lazy val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % "3.9.0"

  lazy val betterModadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  lazy val http4sClient     = "org.http4s"          %% "http4s-ember-client"   % http4sVersion
  lazy val http4sServer     = "org.http4s"          %% "http4s-ember-server"   % http4sVersion
  lazy val http4sDsl        = "org.http4s"          %% "http4s-dsl"            % http4sVersion
  lazy val decline          = "com.monovore"        %% "decline-effect"        % "2.5.0"
  lazy val ce               = "org.typelevel"       %% "cats-effect-std"       % catsEffectVersion
  lazy val awsGlueRegistry  = "software.amazon.glue" % "schema-registry-serde" % "1.1.25"
//  lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.17.9"

  lazy val munit       = "org.scalameta" %% "munit"             % "1.1.1"
  lazy val ceMunitTest = "org.typelevel" %% "munit-cats-effect" % "2.1.0"

  lazy val testContainersScala     = "com.dimafeng" %% "testcontainers-scala-munit" % testcontainersVersion % Test
  lazy val testContainerScalaKafka = "com.dimafeng" %% "testcontainers-scala-kafka" % testcontainersVersion % Test
}
