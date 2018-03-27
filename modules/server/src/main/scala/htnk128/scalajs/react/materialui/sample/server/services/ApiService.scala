package htnk128.scalajs.react.materialui.sample.server.services

import htnk128.scalajs.react.materialui.sample.server.services.sample.SampleApiService
import htnk128.scalajs.react.materialui.sample.shared.api._

trait ApiService extends Api with SampleApiService {

  override def authenticate(loginId: String): Option[User] =
    if (loginId.nonEmpty) Some(User(loginId)) else None
}

object ApiService extends ApiService
