ThisBuild / scalaVersion     := "3.3.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

// App subproject
lazy val App = (project in file("APP"))
  .settings(
    name := "App",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.5",
      "dev.zio" %% "zio-test" % "2.1.5" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  ).dependsOn(Lib)

// Lib subproject
lazy val Lib = (project in file("LIB"))
  .settings(
    name := "Lib",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.5",
      "dev.zio" %% "zio-test" % "2.1.5" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
