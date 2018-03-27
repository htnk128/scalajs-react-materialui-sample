package htnk128.scalajs.react.materialui.sample.client.components

import chandu0101.scalajs.react.components.materialui._
import diode.data.Pot
import diode.react._
import htnk128.scalajs.react.materialui.sample.client.model.MenuItem
import htnk128.scalajs.react.materialui.sample.client.services.AppCircuit
import htnk128.scalajs.react.materialui.sample.shared._
import htnk128.scalajs.react.materialui.sample.shared.api.AppContext
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

object Motd {

  case class Props(selectedPage: MenuItem, ctrl: RouterCtl[MenuItem], proxy: ModelProxy[Pot[AppContext]])

  case class State(wrapper: ReactConnectProxy[Pot[AppContext]])

  private val component = ScalaComponent
    .builder[Props]("Motd")
    .initialStateFromProps(props => State(props.proxy.connect(m => m)))
    .renderP((_, P) =>
      <.div(
        <.h2(P.selectedPage.title),
        MuiCard()(
          MuiCardHeader(title = <.span(<.b("お知らせ")).render)(),
          MuiCardText()(
            <.p(s"ようこそ${appName}へ!")
          )
        )
      )
    )
    .build

  def apply(selectedPage: MenuItem, ctrl: RouterCtl[MenuItem]): VdomElement =
    AppCircuit.wrap(_.app)(p => component(Props(selectedPage, ctrl, p)))
}
