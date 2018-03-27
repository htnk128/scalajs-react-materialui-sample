import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._
import scalariform.formatter.preferences._

import com.typesafe.sbt.SbtScalariform.autoImport._
import org.flywaydb.sbt.FlywayPlugin.autoImport._
import com.typesafe.sbt.web.Import._
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import webscalajs.WebScalaJS.autoImport.scalaJSPipeline

object BuildSettings {

  val commonSettings = Seq(
    organization := "scalajs-react-materialui-sample",
    scalaVersion := "2.12.4",
    scalacOptions ++= Seq(
      "-Xlint",
      "-unchecked",
      "-deprecation",
      "-feature"
    ),
    scalariformPreferences := scalariformPreferences.value
      .setPreference(CompactControlReadability, true)
      .setPreference(DanglingCloseParenthesis, Preserve)
  )

  val shared = commonSettings ++ Seq(
    name := "scalajs-react-materialui-sample-shared",
    libraryDependencies ++= Dependencies.shared.value
  )

  val server = commonSettings ++ Seq(
    name := "scalajs-react-materialui-sample-server",
    libraryDependencies ++= Dependencies.server,
    libraryDependencies ++= Dependencies.jvm,
    pipelineStages in Assets := Seq(scalaJSPipeline),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,
    EclipseKeys.preTasks := Seq(compile in Compile),
    flywayDriver := System.getProperty("flyway.driver", "org.h2.Driver"),
    flywayUrl := System.getProperty("flyway.url", "jdbc:h2:file:~/data/db/development;MODE=MySQL;AUTO_SERVER=TRUE"),
    flywayUser := System.getProperty("flyway.user", "sa"),
    flywayPassword := System.getProperty("flyway.password", "sa"),
    flywayLocations := Seq("filesystem:./modules/server/src/main/resources/db/migration")
  )

  val client = commonSettings ++ Seq(
    name := "scalajs-react-materialui-sample-client",
    mainClass in Compile := Some("htnk128.scalajs.react.materialui.sample.client.AppMain"),
    libraryDependencies ++= Dependencies.client.value,
    npmDependencies in Compile := Dependencies.js,
    npmDevDependencies in Compile := Dependencies.devJs,
    webpackConfigFile in fastOptJS := Some(baseDirectory.value / "dev.webpack.config.js"),
    webpackConfigFile in fullOptJS := Some(baseDirectory.value / "prod.webpack.config.js"),
    scalaJSUseMainModuleInitializer := true,
    scalaJSUseMainModuleInitializer in Test := false,
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv,
    webpackBundlingMode := BundlingMode.LibraryOnly()
  )
}
