package htnk128.scalajs.react.materialui.sample.server.system

import com.typesafe.scalalogging.LazyLogging

object Initializer extends LazyLogging {

  private val lifeCycles = Seq(
    DBLifeCycle(),
    WebAppLifeCycle()
  )

  def start(): Unit = {
    lifeCycles.foreach(_.init())
    logger.info("application start.")
  }

  def stop(): Unit = {
    lifeCycles.foreach(_.destroy())
    logger.info("application end.")
  }
}
