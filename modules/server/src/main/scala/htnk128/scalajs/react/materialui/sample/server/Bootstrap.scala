package htnk128.scalajs.react.materialui.sample.server

import htnk128.scalajs.react.materialui.sample.server.system.Initializer

object Bootstrap extends App {

  sys.addShutdownHook(Initializer.stop())

  Initializer.start()
}
