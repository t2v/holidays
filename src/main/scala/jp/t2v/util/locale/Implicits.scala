package jp.t2v.util.locale

import org.scala_tools.time.Imports._

object Implicits {

  implicit def wrapLocalDate(d: LocalDate) = new {
    def holidayName: Option[String] = Holidays(d)
    def isHoliday: Boolean = holidayName.isDefined
  }

  implicit def wrapDateTime(d: DateTime) = wrapLocalDate(d.toLocalDate)

}
