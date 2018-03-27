package htnk128.scalajs.react.materialui.sample.client.css

import scala.language.postfixOps
import scalacss.ProdDefaults._

object PageStyle extends StyleSheet.Inline {
  import dsl._

  style(unsafeRoot(".box-container")(
    display.flex,
    flexWrap.wrap,
    justifyContent.spaceAround,
    height(80 vh)
  ))

  style(unsafeRoot(".box-list")(
    display.flex,
    flexWrap.nowrap,
    width(100 %%)
  ))

  style(unsafeRoot(".menu-container")(
    width(23 %%)
  ))

  style(unsafeRoot(".main-container")(
    padding(10 px),
    width(77 %%),
    overflowY.scroll
  ))

  style(unsafeRoot(".full-width-main-container")(
    padding(10 px),
    width(100 %%),
    overflowY.scroll,
    marginLeft(-23 %%)
  ))

  style(unsafeRoot(".form-container")(
    width(80 %%),
    margin(0 px, auto)
  ))

  style(unsafeRoot(".form-buttons")(
    maxWidth(100 %%),
    padding(10 px)
  ))

  style(unsafeRoot(".input-file")(
    cursor.pointer,
    position.absolute,
    top(0 px),
    bottom(0 px),
    right(0 px),
    left(0 px),
    width(100 %%),
    opacity(0)
  ))
}
