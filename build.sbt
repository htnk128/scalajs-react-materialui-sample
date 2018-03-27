lazy val shared = (crossProject.crossType(CrossType.Pure) in file("modules/shared"))
  .settings(BuildSettings.shared)
  .jsConfigure(_.enablePlugins(ScalaJSWeb, ScalaJSBundlerPlugin))

lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")

lazy val sharedJS = shared.js.settings(name := "sharedJS")

lazy val server = project.in(file("modules/server"))
  .settings(BuildSettings.server)
  .settings(scalaJSProjects := Seq(client))
  .enablePlugins(WebScalaJSBundlerPlugin, SbtTwirl, JavaAppPackaging, ScalikejdbcPlugin)
  .dependsOn(sharedJVM)

lazy val client: Project = (project in file("modules/client"))
  .settings(BuildSettings.client)
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, ScalaJSWeb)
  .dependsOn(sharedJS)

onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
