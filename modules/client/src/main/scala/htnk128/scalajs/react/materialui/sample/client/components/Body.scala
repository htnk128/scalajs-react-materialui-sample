package htnk128.scalajs.react.materialui.sample.client.components

import scala.language.existentials
import scala.scalajs.js

import chandu0101.scalajs.react.components.materialui._
import htnk128.scalajs.react.materialui.sample.client.model._
import htnk128.scalajs.react.materialui.sample.client.routes.AppRouter.AppPage
import htnk128.scalajs.react.materialui.sample.client.routes.{ AppRouter, RootModule }
import htnk128.scalajs.react.materialui.sample.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.Reusability
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

object Body {

  case class Props(menus: Seq[Menu], selectedPage: AppPage, ctrl: RouterCtl[AppPage])

  case class State(isOpen: Boolean)

  private implicit val currentPageReuse: Reusability[AppPage] = Reusability.by_==[AppPage]

  private implicit val propsReuse: Reusability[Props] = Reusability.by((_: Props).selectedPage)

  private implicit val stateReuse: Reusability[State] = Reusability.derive[State]

  case class Backend(scope: BackendScope[Props, State]) {

    private val menuToggle: Callback = scope.modState(s => s.copy(isOpen = !s.isOpen))

    def render(P: Props, C: PropsChildren, S: State): VdomElement = {
      <.div(
        Header(P.menus, P.selectedPage, P.ctrl, menuToggle),
        Contents(P.menus, P.selectedPage, P.ctrl, menuToggle, S.isOpen)(C),
        Footer()
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("Body")
    .initialState(State(false))
    .renderBackendWithChildren[Backend]
    .configure(Reusability.shouldComponentUpdate)
    .build

  def apply(menus: Seq[Menu], selectedPage: AppPage, ctrl: RouterCtl[AppPage])(children: VdomNode*): VdomElement =
    component(Props(menus, selectedPage, ctrl))(children: _*)
}

object Header {

  case class Props(menus: Seq[Menu], selectedPage: AppPage, ctrl: RouterCtl[AppPage], menuToggle: Callback)

  case class Backend(scope: BackendScope[Props, _]) {

    def render(P: Props): VdomElement = {
      <.header(
        MuiAppBar(
          title = <.span(
            ^.className := "title",
            ^.onClick --> P.ctrl.set(AppRouter.RootPage(RootModule.DashBoard)),
            appName
          ).render,
          showMenuIconButton = true,
          onLeftIconButtonTouchTap = js.defined(_ => P.menuToggle)
        )()
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("Header")
    .renderBackend[Backend]
    .build

  def apply(menus: Seq[Menu], selectedPage: AppPage, ctrl: RouterCtl[AppPage], menuToggle: Callback): VdomElement =
    component(Props(menus, selectedPage, ctrl, menuToggle))
}

object Contents {

  case class Props(menus: Seq[Menu], selectedPage: AppPage, ctrl: RouterCtl[AppPage], menuToggle: Callback, isOpen: Boolean)

  case class Backend(scope: BackendScope[Props, _]) {

    def render(P: Props, C: PropsChildren): VdomElement = {
      <.div(
        ^.className := "box-container",
        <.div(
          ^.className := "box-list",
          MuiDrawer(
            onRequestChange = js.defined((_, _) => P.menuToggle),
            open = P.isOpen,
            docked = true,
            className = "menu-container",
            containerStyle = js.Dynamic.literal(
              overflowY = "scroll",
              overflowX = "hidden",
              position = "static"
            )
          )(
              P.menus
                .map { m =>
                  val sm = m.sub.map(sm =>
                    MuiMenuItem(
                      key = sm.name,
                      primaryText = sm.name,
                      onTouchTap = js.defined(_ => P.ctrl.set(sm.route)),
                      checked = P.selectedPage == sm.route || sm.isBelong(P.selectedPage)
                    )()
                  ).toVdomArray

                  MuiMenuItem(
                    key = m.name,
                    primaryText = <.div(^.marginLeft := "12px", m.name),
                    checked = P.selectedPage == m.route || m.isBelong(P.selectedPage),
                    menuItems = sm
                  )()
                }.toVdomArray
            ),
          <.div(
            ^.className := (if (P.isOpen) "main-container" else "full-width-main-container"),
            C
          )
        )
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("Contents")
    .renderBackendWithChildren[Backend]
    .build

  def apply(
    menus: Seq[Menu],
    selectedPage: AppPage,
    ctrl: RouterCtl[AppPage],
    menuToggle: Callback,
    isOpen: Boolean)(children: VdomNode*): VdomElement =
    component(Props(menus, selectedPage, ctrl, menuToggle, isOpen))(children: _*)
}

object Footer {

  private val component = ScalaComponent
    .builder
    .static("Footer")(
      <.footer(
        ^.textAlign.center,
        <.hr(),
        <.p(<.small("Footer"))
      )
    )
    .build

  def apply(): VdomElement = component()
}
