package htnk128.scalajs.react.materialui.sample.client.components.validation

object Validator {

  def apply(ok: Boolean, message: String): ValidationResult = {
    if (!ok) ValidationResult.withError(message)
    else ValidationResult.Success
  }
}

case class ValidationResult(private val errors: Seq[ValidationError]) {

  def isValid: Boolean = errors.isEmpty

  def errorMessage: String = errors.map(_.message).mkString(", ")

  def errorMessages: Seq[String] = errors.map(_.message)

  def ++(other: ValidationResult): ValidationResult =
    ValidationResult(this.errors.union(other.errors))

  def +(error: ValidationError): ValidationResult = ValidationResult(this.errors :+ error)
}

object ValidationResult {

  val Success = ValidationResult(Seq.empty)

  def withError(message: String) = ValidationResult(Seq(ValidationError(message)))
}

case class ValidationError(message: String)

