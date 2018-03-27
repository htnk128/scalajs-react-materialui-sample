package htnk128.scalajs.react.materialui.sample.client

import scala.scalajs.js
import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

import htnk128.scalajs.react.materialui.sample.client.components.internal.ReactTapEventPlugin
import htnk128.scalajs.react.materialui.sample.client.css.AppCSS
import htnk128.scalajs.react.materialui.sample.client.routes.AppRouter
import org.scalajs.dom

@JSExportTopLevel("AppMain")
object AppMain {

  ReactTapEventPlugin(js.undefined)

  @JSExport
  def main(): Unit = {
    AppCSS.load()
    AppRouter.router().renderIntoDOM(dom.document.getElementById("root"))
  }
}
