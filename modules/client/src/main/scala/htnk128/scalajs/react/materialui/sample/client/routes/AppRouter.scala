package htnk128.scalajs.react.materialui.sample.client.routes

import chandu0101.scalajs.react.components.materialui.MuiMuiThemeProvider
import htnk128.scalajs.react.materialui.sample.client.components.Body
import htnk128.scalajs.react.materialui.sample.client.model.MenuItem
import japgolly.scalajs.react.extra.router._
import htnk128.scalajs.react.materialui.sample.client.model.Menu

object AppRouter {

  sealed trait AppPage {
    val mi: MenuItem
  }

  case class RootPage(mi: MenuItem) extends AppPage

  case class SamplePage(mi: MenuItem) extends AppPage

  val config: RouterConfig[AppPage] = RouterConfigDsl[AppPage].buildConfig { dsl =>
    import dsl._

    (trimSlashes
      | RootModule.routes.pmap[AppPage](RootPage) { case RootPage(mi) => mi }
      | SampleModule.routes.prefixPath_/(SampleModule.prefixPath).pmap[AppPage](SamplePage) { case SamplePage(mi) => mi }
    )
      .notFound(redirectToPage(RootPage(RootModule.DashBoard))(Redirect.Replace))
      .renderWith(layout)
  }

  val mainMenu: Seq[Menu] = Seq(
    Menu("サンプル", SamplePage(SampleModule.Root), SampleModule.subMenu, {
      case SamplePage(_) => true
      case _ => false
    })
  )

  private def layout(c: RouterCtl[AppPage], r: Resolution[AppPage]) = {
    MuiMuiThemeProvider()(
      Body(mainMenu, r.page, c)(r.render())
    ).vdomElement
  }

  val baseUrl: BaseUrl = BaseUrl.fromWindowOrigin

  val router = Router(baseUrl, config)
}
