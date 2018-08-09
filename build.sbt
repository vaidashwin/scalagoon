name := "scalagoon"

version := "1.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.bintrayRepo("hseeberger", "maven")

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.4.1",
  "com.typesafe.play" %% "play-json" % "2.6.7"
)

scalaSource in Compile <<= baseDirectory(_ / "src")

mainClass in (Compile, run) := Some("app.Robot")

fork in run := true