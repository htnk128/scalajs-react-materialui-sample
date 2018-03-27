package htnk128.scalajs.react.materialui.sample.client.components

import scala.collection.immutable
import scala.scalajs.js
import scalacss.ProdDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

import chandu0101.scalajs.react.components.materialui.MuiSvgIcon._
import chandu0101.scalajs.react.components.materialui.{ MuiGridTile, _ }
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

/**
 * Companion object of ReactTable, with tons of little utilities
 * This software includes the work that is distributed in the Apache License 2.0
 */
object DataTable {

  object Style extends StyleSheet.Inline {
    import dsl._

    val container = style(
      display.flex,
      flexWrap.wrap,
      justifyContent.spaceAround
    )

    val pagination = style(float.right)
  }

  object SortDirection extends Enumeration {
    type SortDirection = Value
    val asc, dsc = Value
  }

  def DefaultOrdering[T, B](fn: T => B)(implicit ordering: Ordering[B]): Ordering[T] {
  } = new Ordering[T] {
    def compare(a: T, b: T): RowId = ordering.compare(fn(a), fn(b))
  }

  def ignoreCaseStringOrdering[T](fn: T => String): Ordering[T] {
  } = new Ordering[T] {
    def compare(a: T, b: T): RowId = fn(a).compareToIgnoreCase(fn(b))
  }

  type CellRenderer[T] = T => VdomNode

  def DefaultCellRenderer[T]: CellRenderer[T] = { model =>
    <.span(model.toString)
  }

  def EmailRenderer[T](fn: T => String): CellRenderer[T] = { t =>
    val s = fn(t)
    <.a(^.whiteSpace.nowrap, ^.href := s"mailto:$s", s)
  }

  def OptionRenderer[T, B](defaultValue: VdomNode = "", bRenderer: CellRenderer[B])(fn: T => Option[B]): CellRenderer[T] =
    t => fn(t).fold(defaultValue)(bRenderer)

  case class ColumnConfig[T](
    name: String,
    cellRenderer: CellRenderer[T],
    width: Option[String] = None,
    sortable: Boolean = false,
    nowrap: Boolean = false)(implicit val ordering: Ordering[T] = DefaultOrdering[T, String](cellRenderer => cellRenderer.toString))

  def SimpleStringConfig[T](
    name: String,
    stringRetriever: T => String,
    width: Option[String] = None,
    sortable: Boolean = false,
    nowrap: Boolean = false): DataTable.ColumnConfig[T] = {
    val renderer: CellRenderer[T] = if (nowrap) { t =>
      <.span(stringRetriever(t))
    }
    else { t =>
      stringRetriever(t)
    }
    ColumnConfig(name, renderer, width, sortable, nowrap)(ignoreCaseStringOrdering(stringRetriever))
  }
}

case class DataTable[T](
  data: Seq[T],
  configs: Seq[DataTable.ColumnConfig[T]] = Seq(),
  rowsPerPage: Int = 25) {

  import DataTable._
  import SortDirection._

  case class State(
    filterText: String,
    offset: Int,
    rowsPerPage: Int,
    filteredData: Seq[T],
    sortedState: Map[Int, SortDirection]
  )

  case class ChoiceId(value: String)

  case class Choice(id: ChoiceId, text: String)

  class Backend(scope: BackendScope[Props, State]) {

    def onPreviousClick: Callback = scope.modState(s => s.copy(offset = s.offset - s.rowsPerPage))

    def onNextClick: Callback = scope.modState(s => s.copy(offset = s.offset + s.rowsPerPage))

    def sort(ordering: Ordering[T], columnIndex: Int): Callback =
      scope.modState { state =>
        val rows = state.filteredData
        state.sortedState.get(columnIndex) match {
          case Some(SortDirection.asc) =>
            state.copy(
              filteredData = rows.sorted(ordering.reverse),
              sortedState = Map(columnIndex -> dsc),
              offset = 0
            )
          case _ =>
            state.copy(
              filteredData = rows.sorted(ordering),
              sortedState = Map(columnIndex -> asc),
              offset = 0
            )
        }
      }

    def onPageSizeChange(e: TouchTapEvent, index: Int, value: Choice): Callback =
      scope.modState(_.copy(rowsPerPage = value.id.value.toInt))

    def render(P: Props, S: State): VdomElement = {
      def rowsPerPage = {
        val total = S.filteredData.length
        if (total > P.rowsPerPage) {
          val choices: Seq[Choice] = immutable.Range
            .inclusive(P.rowsPerPage, total, 100)
            .:+(total)
            .map { i =>
              Choice(ChoiceId(i.toString), i.toString)
            }
          val selected = choices
            .find(_.id == ChoiceId(S.rowsPerPage.toString))
            .getOrElse(choices.head)

          MuiSelectField[Choice](
            floatingLabelText = "",
            value = selected,
            onChange = { (e: TouchTapEvent, index: Int, value: Choice) => onPageSizeChange(e, index, value) },
            style = js.Dynamic.literal(
              "fontSize" -> "14px",
              "width" -> "100px"
            )
          )(
              choices
                .map(c => MuiMenuItem[Choice](key = c.id.value, value = c, primaryText = c.text)())
                .toVdomArray
            ).vdomElement
        }
        else VdomArray.empty()
      }

      def settingsBar = {
        val total = S.filteredData.length
        val displayStart = S.offset + 1
        val displayEnd = if (S.offset == 0) S.rowsPerPage else {
          (S.offset / S.rowsPerPage + 1) * S.rowsPerPage match {
            case end if end > total => total
            case other => other
          }
        }

        <.div(
          Style.container,
          MuiGridList(
            style = js.Dynamic.literal(
              "display" -> "flex",
              "flexWrap" -> "nowrap",
              "overflowX" -> "auto",
              "width" -> "100%",
              "paddingTop" -> "10px"
            ),
            cellHeight = 60,
            cols = 2,
            padding = 0
          )(
              MuiGridTile(style = js.Dynamic.literal("display" -> "flex", "flexWrap" -> "nowrap", "overflowX" -> "auto"))(
                rowsPerPage,
                <.p(^.fontSize := "14px", s"$total 件中 $displayStart 件から $displayEnd 件までを表示")
              ),
              MuiGridTile(style = js.Dynamic.literal("paddingRight" -> "3px"))(
                <.div(
                  Style.pagination,
                  Pagination(S.rowsPerPage, S.filteredData.length, S.offset, _ => onNextClick, _ => onPreviousClick)
                )
              )
            )
        )
      }

      def columnWidth(config: ColumnConfig[T]): js.Any =
        config.width.map(w => js.Dynamic.literal("width" -> w)).getOrElse(js.undefined)

      def headerColumn =
        MuiTableRow()(
          P.configs.zipWithIndex.map {
            case (config, index) =>
              MuiTableHeaderColumn(
                key = index.toString,
                tooltip = config.name,
                columnNumber = index,
                style = columnWidth(config)
              )(
                  config.name,
                  if (config.sortable) {
                    MuiIconButton(
                      onClick = { (_: ReactEvent) => sort(config.ordering, index) }
                    )(Mui.SvgIcons.ContentSort()())
                  }
                  else VdomArray.empty()
                )
          }.toVdomArray
        )

      def renderHeader =
        MuiTableHeader(
          displaySelectAll = false,
          adjustForCheckbox = false,
          enableSelectAll = false
        )(headerColumn)

      def renderRow(model: T, rowIndex: Int) =
        MuiTableRow(key = rowIndex.toString)(
          P.configs.zipWithIndex
            .map {
              case (config, index) =>
                MuiTableRowColumn(key = index.toString, style = columnWidth(config))(config.cellRenderer(model))
            }
            .toVdomArray
        )

      val rows = S.filteredData
        .slice(S.offset, S.offset + S.rowsPerPage)
        .zipWithIndex
        .map {
          case (row, i) => renderRow(row, i)
        }

      def renderBody =
        MuiTableBody(
          displayRowCheckbox = false,
          deselectOnClickaway = false,
          showRowHover = true,
          stripedRows = true
        )(rows.toVdomArray)

      def renderFooter =
        MuiTableFooter(adjustForCheckbox = false)(headerColumn)

      MuiMuiThemeProvider()(
        <.div(
          settingsBar,
          MuiTable(
            fixedHeader = false,
            fixedFooter = false,
            selectable = false,
            multiSelectable = false
          )(
            renderHeader,
            renderBody,
            renderFooter
          ),
          settingsBar
        )
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("DataTable")
    .initialStateFromProps(props => State(filterText = "", offset = 0, props.rowsPerPage, props.data, Map()))
    .renderBackend[Backend]
    .build

  case class Props(
    data: Seq[T],
    configs: Seq[ColumnConfig[T]],
    rowsPerPage: Int
  )

  def apply(): VdomElement = component(Props(data, configs, rowsPerPage))
}

object Pagination {

  case class Props(
    itemsPerPage: Int,
    totalItems: Int,
    offset: Int,
    nextClick: ReactEvent => Callback,
    previousClick: ReactEvent => Callback)

  class Backend(t: BackendScope[Props, _]) {

    def render(P: Props): VdomElement =
      <.div(
        MuiRaisedButton(
          label = "",
          onClick = P.previousClick,
          icon = js.defined(Mui.SvgIcons.HardwareKeyboardArrowLeft()())
        )().when(P.offset > 0),
        MuiRaisedButton(
          label = "",
          onClick = P.nextClick,
          icon = js.defined(Mui.SvgIcons.HardwareKeyboardArrowRight()())
        )().when(P.offset + P.itemsPerPage < P.totalItems)
      )
  }

  private val component = ScalaComponent
    .builder[Props]("Pagination")
    .renderBackend[Backend]
    .build

  def apply(
    itemsPerPage: Int,
    totalItems: Int,
    offset: Int,
    nextClick: ReactEvent => Callback,
    previousClick: ReactEvent => Callback): VdomElement =
    component(Props(itemsPerPage, totalItems, offset, nextClick, previousClick))
}
