package htnk128.scalajs.react.materialui.sample.shared.api

import htnk128.scalajs.react.materialui.sample.shared.api.sample.SampleApi

trait Api extends SampleApi {

  def authenticate(loginId: String): Option[User]
}
