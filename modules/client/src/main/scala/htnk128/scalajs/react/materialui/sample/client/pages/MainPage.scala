package htnk128.scalajs.react.materialui.sample.client.pages

import htnk128.scalajs.react.materialui.sample.client.model.MenuItem
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

object MainPage {

  case class Props(selectedPage: MenuItem, ctrl: RouterCtl[MenuItem])

  private val component = ScalaComponent
    .builder[Props]("MainPage")
    .render_P(P => P.selectedPage.render(P.selectedPage, P.ctrl))
    .build

  def apply(selectedPage: MenuItem, ctrl: RouterCtl[MenuItem]): VdomElement =
    component(Props(selectedPage, ctrl))
}
