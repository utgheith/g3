val scala3Version = "3.0.0-RC2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "g3",
    version := "0.1.0",

    scalaVersion := scala3Version,
    scalacOptions ++= Seq("-Yexplicit-nulls"),

    libraryDependencies ++= Seq(
      //"com.lihaoyi" %% "os-lib" % "0.7.4",
      "com.hierynomus" % "sshj" % "0.31.0",
      "com.lihaoyi" %% "geny" % "0.6.8",
      "com.typesafe" % "config" % "1.4.1",
      //"com.github.rssh" %% "dotty-cps-async" % "0.5.0",
      "org.scalacheck" %% "scalacheck" % "1.15.3" % Test
    )
  )
