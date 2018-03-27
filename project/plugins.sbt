resolvers += "Flyway" at "https://flywaydb.org/repo"

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.19")

addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.5")

addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.8.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.3.12")

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.45"

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.1.0")

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.2.0")

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC12")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.2")
