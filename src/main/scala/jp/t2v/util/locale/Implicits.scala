package jp.t2v.util.locale

import com.github.nscala_time.time.Imports._

object Implicits {

  implicit def wrapLocalDate(d: LocalDate) = new LocalDateWrapper(d)

  implicit def wrapDateTime(d: DateTime) = wrapLocalDate(d.toLocalDate)

}
class LocalDateWrapper(d: LocalDate) {
  def holidayName: Option[String] = Holidays(d)
  def isHoliday: Boolean = holidayName.isDefined
}
