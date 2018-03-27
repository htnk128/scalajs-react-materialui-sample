package htnk128.scalajs.react.materialui.sample.client.model

import htnk128.scalajs.react.materialui.sample.client.routes.AppRouter

case class Menu(
  name: String,
  route: AppRouter.AppPage,
  sub: Seq[Menu] = Seq.empty,
  isBelong: AppRouter.AppPage => Boolean = _ => false
)
