package jp.t2v.util.locale

import java.time.{LocalDate, LocalDateTime, ZonedDateTime}

@FunctionalInterface
abstract class LocalDateConverter[A] {
  def apply(value: A): LocalDate
}
object LocalDateConverter extends LowPriorityLocalDateConverter {
  def apply[A](f: A => LocalDate): LocalDateConverter[A] = new LocalDateConverter[A] {
    def apply(value: A): LocalDate = f(value)
  }
  implicit val localDate: LocalDateConverter[LocalDate] = LocalDateConverter[LocalDate](identity)
  implicit val localDateTime: LocalDateConverter[LocalDateTime] = LocalDateConverter[LocalDateTime](_.toLocalDate)
  implicit val zonedDateTime: LocalDateConverter[ZonedDateTime] = LocalDateConverter[ZonedDateTime](_.toLocalDate)
}

trait LowPriorityLocalDateConverter {
  implicit lazy val jodaLocalDateConverter: LocalDateConverter[org.joda.time.LocalDate] =
    new LocalDateConverter[org.joda.time.LocalDate] {
      def apply(d: org.joda.time.LocalDate) = java.time.LocalDate.of(d.getYear, d.getMonthOfYear, d.getDayOfMonth)
    }

  implicit lazy val jodaLocalTimeConverter: LocalDateConverter[org.joda.time.DateTime] =
    new LocalDateConverter[org.joda.time.DateTime] {
      def apply(value: org.joda.time.DateTime) = {
        val d = value.toLocalDate
        java.time.LocalDate.of(d.getYear, d.getMonthOfYear, d.getDayOfMonth)
      }
    }
}
