import Dependencies._
import Util._

ThisBuild / organization := "dev.insideyou"
ThisBuild / scalaVersion := "3.1.1"

ThisBuild / scalacOptions ++=
  Seq(
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yexplicit-nulls", // experimental (I've seen it cause issues with circe)
    "-Ykind-projector",
    "-Ysafe-init", // experimental (I've seen it cause issues with circe)
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future-migration")

lazy val `invoice-generator` =
  project
    .in(file("."))
    .settings(name := "invoice-generator")
    .settings(commonSettings)
    .aggregate(
      `core-self`,
      `third-party-dependencies`,
      `type-classes`,
      config,
      core,
      main,
      terminal,
    )

lazy val `type-classes` =
  project
    .in(file("00-type-classes"))
    .settings(commonSettings)
    .settings(dependencies)

lazy val core =
  project
    .in(file("01-core"))
    .settings(commonSettings)
    .settings(dependencies)
    .dependsOn(`type-classes` % Cctt)
    .dependsOn(`third-party-dependencies` % "test->compile")

lazy val `third-party-dependencies` =
  project
    .in(file("01-third-party-dependencies"))
    .settings(commonSettings)
    .dependsOn(`type-classes` % Cctt)

lazy val config =
  project
    .in(file("02-b-config"))
    .settings(commonSettings)
    .dependsOn(core % Cctt)
    .dependsOn(`third-party-dependencies` % Cctt)

lazy val `core-self` =
  project
    .in(file("02-b-core-self"))
    .settings(commonSettings)
    .dependsOn(core % Cctt)
    .dependsOn(`third-party-dependencies` % Cctt)

lazy val terminal =
  project
    .in(file("02-a-terminal"))
    .settings(commonSettings)
    .dependsOn(core % Cctt)
    .dependsOn(`third-party-dependencies` % Cctt)

lazy val main =
  project
    .in(file("03-main"))
    .settings(commonSettings)
    .dependsOn(config % Cctt)
    .dependsOn(`core-self` % Cctt)
    .dependsOn(terminal % Cctt)

lazy val commonSettings = commonScalacOptions ++ Seq(
  update / evictionWarningOptions := EvictionWarningOptions.empty
)

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings",
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value,
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    // main dependencies
  ),
  libraryDependencies ++= Seq(
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-15`,
  ).map(_ % Test),
)
