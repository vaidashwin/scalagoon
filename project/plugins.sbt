logLevel := Level.Warn

resolvers += Classpaths.sbtPluginReleases
resolvers += Classpaths.typesafeReleases

addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")