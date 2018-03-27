package htnk128.scalajs.react.materialui.sample.client.model

import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement

abstract class MenuItem(
  val title: String,
  val routerPath: String,
  val render: (MenuItem, RouterCtl[MenuItem]) => VdomElement
)
