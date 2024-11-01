import sbt._

object Dependencies {
  lazy val munit = "org.scalameta" %% "munit" % "0.7.29"
  lazy val kafkaClients38 = "org.apache.kafka" % "kafka-clients" % "3.8.0"
  lazy val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"
}
