ThisBuild / scalaVersion     := "3.3.3"
ThisBuild / version          := "1.0.0"
ThisBuild / organizationName := "Dre-Te"

// App subproject
lazy val App = (project in file("APP"))
  .settings(
    name := "App",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.5",
      "dev.zio" %% "zio-test" % "2.1.5" % Test,
      "dev.zio" %% "zio-json" % "0.7.1"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  ).dependsOn(Lib)

// Lib subproject
lazy val Lib = (project in file("LIB"))
  .settings(
    name := "Lib",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.5",
      "dev.zio" %% "zio-test" % "2.1.5" % Test,
      "dev.zio" %% "zio-json" % "0.7.1",
      "org.scalatest" %% "scalatest" % "3.2.18" %Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
)
