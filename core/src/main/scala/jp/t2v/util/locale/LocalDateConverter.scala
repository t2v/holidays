package jp.t2v.util.locale

import java.time.{LocalDate, LocalDateTime, ZonedDateTime}

@FunctionalInterface
abstract class LocalDateConverter[A] {
  def apply(value: A): LocalDate
}
object LocalDateConverter extends LowPriorityLocalDateConverter {
  implicit val localDate: LocalDateConverter[LocalDate] = d => d
  implicit val localDateTime: LocalDateConverter[LocalDateTime] = _.toLocalDate
  implicit val zonedDateTime: LocalDateConverter[ZonedDateTime] = _.toLocalDate
}

trait LowPriorityLocalDateConverter {
  import scala.language.experimental.macros
  implicit def jodaLocalDateConverter[A]: LocalDateConverter[A] = macro LocalDateConverterMacro.joda[A]
}

private[locale] object LocalDateConverterMacro {
  import scala.reflect.macros.blackbox

  def joda[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[LocalDateConverter[A]] = {
    import c.universe._
    val A = weakTypeTag[A].tpe
    val expr = A.toString match {
      case "org.joda.time.LocalDate" =>
        q"""new jp.t2v.util.locale.LocalDateConverter[$A] {
          def apply(d: $A) = java.time.LocalDate.of(d.getYear, d.getMonthOfYear, d.getDayOfMonth)
        }"""
      case "org.joda.time.DateTime" =>
        q"""new jp.t2v.util.locale.LocalDateConverter[$A] {
          def apply(value: $A) = {
            val d = value.toLocalDate
            java.time.LocalDate.of(d.getYear, d.getMonthOfYear, d.getDayOfMonth)
          }
        }"""
      case _ =>
        c.abort(c.enclosingPosition, s"Implicit LocalDateConverter[$A] is missing.")
    }
    c.Expr[LocalDateConverter[A]](expr)
  }

}