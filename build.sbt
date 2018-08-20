import sbt.Keys._
import com.github.retronym.SbtOneJar._

name := "scalagoon"

version := "1.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.bintrayRepo("hseeberger", "maven")

oneJarSettings

lazy val scalagoon = (project in file("."))
  .settings(
    scalaVersion := "2.12.6",
    name := "ScalaGoon",
    libraryDependencies ++= Seq(
      "org.scalaj" %% "scalaj-http" % "2.4.1",
      "com.typesafe.play" %% "play-json" % "2.6.7",
      "org.scala-lang.modules" %% "scala-xml" % "1.1.0"
    ),
    scalaSource in Compile <<= baseDirectory(_ / "src"),
    mainClass in (Compile, run) := Some("app.Robot"),
    mainClass in (Compile, packageBin) := Some("app.Robot"),
    fork in run := true
  )



