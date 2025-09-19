import Dependencies.*

lazy val root = (project in file("."))
  .settings(
    name := "stormGen",
    scalaVersion := scalaV,
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
      ce,
      awsGlueRegistry,
//      pureConfig,

      munit % Test,
      ceMunitTest % Test
    ),
    Compile / run / fork := true
  )
  .enablePlugins(JavaAppPackaging)

lazy val itTest = (project in file("./it-test"))
  .settings(
    name := "stormGen-itTest",
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      munit,
      testContainersScala,
      testContainerScalaKafka
    ),
    Test / fork := true
  )
