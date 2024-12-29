import sbt._

object Dependencies {
  lazy val pekkoVersion = "1.1.0"

  lazy val munit = "org.scalameta" %% "munit" % "0.7.29"
  lazy val kafkaClients39 = "org.apache.kafka" % "kafka-clients" % "3.9.0"
  lazy val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"
  lazy val pekkoActor = "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion
}
