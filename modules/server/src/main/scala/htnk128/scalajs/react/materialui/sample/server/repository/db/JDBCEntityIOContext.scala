package htnk128.scalajs.react.materialui.sample.server.repository.db

import htnk128.scalajs.react.materialui.sample.server.repository.EntityIOContext
import scalikejdbc.DBSession

case class JDBCEntityIOContext(session: DBSession) extends EntityIOContext

