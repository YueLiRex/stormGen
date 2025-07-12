import Dependencies.*

lazy val root = (project in file("."))
  .settings(
    name := "stormGen",
    scalaVersion := "2.13.16",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= Seq("-Wunused:imports", "-Dcats.effect.warnOnNonMainThreadDetected=false"),
    libraryDependencies ++= Seq(
      typeSafeConfig,
      shapeless,
      fs2Kafka,
      betterModadicFor,
      http4sServer,
      http4sDsl,
      decline,

      ceTest,
      ceMunitTest,
      munit
    ),
    Compile / run / fork := true
  ).enablePlugins(JavaAppPackaging)
