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

      munit       % Test,
      ceMunitTest % Test
    ),
    Compile / run / fork := true,
    credentials += Credentials(
      "GitHub Package Registry",
      "maven.pkg.github.com",
      System.getenv("GITHUB_USERNAME"),
      System.getenv("GITHUB_SECRET")
    ),
    publishTo := Some("GitHub Package Registry" at "https://maven.pkg.github.com/YueLiRex/stormGen")
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

lazy val example = (project in file("./example"))
  .settings(
    name := "stormGen-example",
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
    ),
    Test / fork := true
  )
