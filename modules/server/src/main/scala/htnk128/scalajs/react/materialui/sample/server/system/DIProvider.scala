package htnk128.scalajs.react.materialui.sample.server.system

import com.google.inject.Guice

object DIProvider {

  lazy private val injector = Guice.createInjector(new Module())

  def get[A](clazz: Class[A]): A = injector.getInstance[A](clazz)
}
