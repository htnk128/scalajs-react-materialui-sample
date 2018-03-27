package htnk128.scalajs.react.materialui.sample.server.system

import com.google.inject.AbstractModule
import htnk128.scalajs.react.materialui.sample.server.model.scalikejdbc.Sample
import htnk128.scalajs.react.materialui.sample.server.repository.SampleRepository
import htnk128.scalajs.react.materialui.sample.server.repository.db.SampleJDBCRepository
import htnk128.scalajs.react.materialui.sample.server.services.ApiService

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ApiService]).toInstance(ApiService)
    bind(classOf[SampleRepository[Sample]]).toInstance(new SampleJDBCRepository)
  }
}

