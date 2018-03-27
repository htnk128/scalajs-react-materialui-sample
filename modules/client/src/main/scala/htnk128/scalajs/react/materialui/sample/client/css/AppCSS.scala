package htnk128.scalajs.react.materialui.sample.client.css

import scalacss.ProdDefaults._
import scalacss.internal.mutable.GlobalRegistry

import htnk128.scalajs.react.materialui.sample.client.components.DataTable

object AppCSS {

  def load(): Unit = {
    GlobalRegistry.register(
      GlobalStyle,
      PageStyle,
      DataTable.Style
    )
    GlobalRegistry.onRegistration(_.addToDocument())
  }
}
