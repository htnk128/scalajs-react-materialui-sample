package htnk128.scalajs.react.materialui.sample.client.css

import scala.language.postfixOps
import scalacss.ProdDefaults._

object GlobalStyle extends StyleSheet.Inline {
  import dsl._

  style(unsafeRoot("html")(
    height(100 %%)
  ))

  style(unsafeRoot("body")(
    height(100 %%),
    width(100 %%),
    padding(0 px),
    margin(0 px)
  ))

  style(unsafeRoot("header .title")(
    cursor.pointer
  ))

  style(unsafeRoot("h2")(
    paddingBottom(.3 em),
    fontSize(1.5 em),
    lineHeight(1.334),
    borderBottom(1 px, solid, c"#eee"),
    marginBottom(16 px),
    fontWeight._400,
    color(rgba(0, 0, 0, .87))
  ))

  style(unsafeRoot("footer hr")(
    border(none),
    borderBottom(solid, 1 px, c"#e0e0e0"),
    marginTop(0 px),
    marginBottom(18 px),
    boxSizing.contentBox,
    height(0 px)
  ))
}
