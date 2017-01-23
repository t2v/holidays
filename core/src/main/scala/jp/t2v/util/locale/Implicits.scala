package jp.t2v.util.locale

object Implicits {

  implicit class HasHolidayNameOps[A](val a: A)(implicit ev: LocalDateConverter[A]) {
    def holidayName: Option[String] = Holidays(ev(a))
    def isHoliday: Boolean = holidayName.isDefined
  }

}
