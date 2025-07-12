import sbt._

object Dependencies {
  lazy val circeVersion = "0.14.10"
  lazy val catsEffectVersion = "3.6.0"
  lazy val http4sVersion = "0.23.30"

  lazy val typeSafeConfig = "com.typesafe" % "config" % "1.4.3"
  lazy val kafkaClients39 = "org.apache.kafka" % "kafka-clients" % "3.9.0"
  lazy val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"

  lazy val fs2Kafka =  "com.github.fd4s" %% "fs2-kafka" % "3.7.0"

  lazy val betterModadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  lazy val http4sClient = "org.http4s" %% "http4s-ember-client" % http4sVersion
  lazy val http4sServer = "org.http4s" %% "http4s-ember-server" % http4sVersion
  lazy val http4sDsl = "org.http4s" %% "http4s-dsl"          % http4sVersion
  lazy val decline = "com.monovore" %% "decline-effect" % "2.5.0"

  lazy val ceTest = "org.typelevel" %% "cats-effect-std" % catsEffectVersion % Test
  lazy val ceMunitTest = "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test
  lazy val munit = "org.scalameta" %% "munit" % "0.7.29" % Test
}
