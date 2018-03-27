package htnk128.scalajs.react.materialui.sample.client.components.validation

trait FormField[V] {

  val getValue: () => V

  val validator: V => ValidationResult

  def validate(): ValidationResult

  def currentValidationResult: Option[ValidationResult]
}

case class DefaultFormField[V](
  getValue: () => V,
  validator: V => ValidationResult = (_: V) => ValidationResult.Success
) extends FormField[V] {

  private var currentVr: Option[ValidationResult] = None

  def validate(): ValidationResult = {
    val vr = validator(getValue())
    currentVr = Some(vr)
    vr
  }

  def currentValidationResult: Option[ValidationResult] = currentVr
}
