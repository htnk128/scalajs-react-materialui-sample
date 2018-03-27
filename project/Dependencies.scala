import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies {

  lazy val akkaHttpVersion = "10.0.10"
  lazy val akkaVersion = "2.5.6"
  lazy val logbackVersion = "1.2.3"
  lazy val guiceVersion = "4.1.0"
  lazy val scalajsReactVersion = "1.1.1"
  lazy val scalaCssVersion = "0.5.3"
  lazy val diodeVersion = "1.1.2"
  lazy val autowireVersion = "0.2.6"
  lazy val booPickleVersion = "1.2.6"
  lazy val scalajsDomVersion = "0.9.3"
  lazy val scalikejdbcVersion = "3.1.0"

  lazy val reactVersion = "15.5.0"
  lazy val materialUiVersion = "0.19.4"

  lazy val scalajsScriptsVersion = "1.1.1"

  val shared = Def.setting(Seq(
    "com.lihaoyi" %%% "autowire" % autowireVersion,
    "io.suzaku" %%% "boopickle" % booPickleVersion
  ))

  val jvm = Seq(
    "com.vmunier" %% "scalajs-scripts" % scalajsScriptsVersion
  )

  val server = Seq(
    "com.typesafe" % "config" % "1.3.2",
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
    "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
    "com.h2database" % "h2" % "1.4.196",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "ch.qos.logback" % "logback-core" % logbackVersion,
    "org.codehaus.janino" % "janino" % "3.0.6",
    "com.google.inject" % "guice" % guiceVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test,
    "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcVersion % Test
  )

  val client = Def.setting(Seq(
    "com.github.japgolly.scalajs-react" %%% "core" % scalajsReactVersion,
    "com.github.japgolly.scalajs-react" %%% "extra" % scalajsReactVersion,
    "com.olvind" %%% "scalajs-react-components" % "0.8.0",
    "com.github.japgolly.scalacss" %%% "core" % scalaCssVersion,
    "com.github.japgolly.scalacss" %%% "ext-react" % scalaCssVersion,
    "io.suzaku" %%% "diode" % diodeVersion,
    "io.suzaku" %%% "diode-react" % diodeVersion,
    "org.scala-js" %%% "scalajs-dom" % scalajsDomVersion
  ))

  val js = Seq(
    "react" -> reactVersion,
    "react-dom" -> reactVersion,
    "material-ui" -> materialUiVersion,
    "react-tap-event-plugin" -> "2.0.1"
  )

  val devJs = Seq(
    "webpack-merge" -> "4.1.0",
    "expose-loader" -> "0.7.1"
  )
}
