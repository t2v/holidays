//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
//_/
//_/　CopyRight(C) K.Tsunoda(AddinBox) 2001 All Rights Reserved.
//_/　( http://www.h3.dion.ne.jp/~sakatsu/index.htm )
//_/
//_/　　この祝日マクロは『kt関数アドイン』で使用しているものです。
//_/　　このロジックは、レスポンスを第一義として、可能な限り少ない
//_/　  【条件判定の実行】で結果を出せるように設計してあります。
//_/　　この関数では、２００７年施行の改正祝日法(昭和の日)までを
//_/　  サポートしています(９月の国民の休日を含む)。
//_/
//_/　(*1)このマクロを引用するに当たっては、必ずこのコメントも
//_/　　　一緒に引用する事とします。
//_/　(*2)他サイト上で本マクロを直接引用する事は、ご遠慮願います。
//_/　　　【 http://www.h3.dion.ne.jp/~sakatsu/holiday_logic.htm 】
//_/　　　へのリンクによる紹介で対応して下さい。
//_/　(*3)[ktHolidayName]という関数名そのものは、各自の環境に
//_/　　　おける命名規則に沿って変更しても構いません。
//_/　
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/

package jp.t2v.util.locale

import org.scala_tools.time.Imports._
import org.joda.time.DateTimeConstants._

object Holidays extends (LocalDate => Option[String]) {

  private val 祝日法施行         = new LocalDate(1948, JULY,     20)
  private val 明仁親王の結婚の儀 = new LocalDate(1959, APRIL,    10)
  private val 昭和天皇大喪の礼   = new LocalDate(1989, FEBRUARY, 24)
  private val 徳仁親王の結婚の儀 = new LocalDate(1993, JUNE,     9)
  private val 即位礼正殿の儀     = new LocalDate(1990, NOVEMBER, 12)
  private val 振替休日施行       = new LocalDate(1973, APRIL,    12)

  private sealed abstract class DayOfWeek(val ordinal: Int)
  private case object Monday extends DayOfWeek(1)
  private case object Tuesday extends DayOfWeek(2)
  private case object Wednesday extends DayOfWeek(3)
//  private case object Thursday extends DayOfWeek(3)
//  private case object Friday extends DayOfWeek(5)
//  private case object Saturday extends DayOfWeek(6)
//  private case object Sunday extends DayOfWeek(7)

  private class LocalDateWrapper(d: LocalDate) {
    def is(w: DayOfWeek): Boolean = d.getDayOfWeek == w.ordinal
  }
  private implicit def wrapLocalDate(d: LocalDate) = new LocalDateWrapper(d)
  
  private class BooleanWrapper(b: Boolean) {
    def ?[X](s: => X) = new {
      def |[Y >: X](n: => Y): Y = if (b) s else n
    }
    def opt[A](s: => A): Option[A] = if (b) Some(s) else None
  }
  private implicit def wrapBoolean(b: Boolean) = new BooleanWrapper(b)
  
  private def springEquinox(year: Int): Int = {
    if (year <= 1947) return 99
    if (year <= 1979) return (20.8357 + (0.242194 * (year - 1980)) - ((year - 1983) / 4)).toInt
    if (year <= 2099) return (20.8431 + (0.242194 * (year - 1980)) - ((year - 1980) / 4)).toInt
    if (year <= 2150) return (21.851  + (0.242194 * (year - 1980)) - ((year - 1980) / 4)).toInt
    99
  }
  private def autumnEquinox(year: Int): Int = {
    if (year <= 1947) return 99
    if (year <= 1979) return (23.2588 + (0.242194 * (year - 1980)) - ((year - 1983) / 4)).toInt
    if (year <= 2099) return (23.2488 + (0.242194 * (year - 1980)) - ((year - 1980) / 4)).toInt
    if (year <= 2150) return (24.2488 + (0.242194 * (year - 1980)) - ((year - 1980) / 4)).toInt
    99
  }

  def apply(target: LocalDate): Option[String] = {
    def holidayName(d: LocalDate): Option[String] = {
      if (d < 祝日法施行) return None
      val year = d.getYear
      val month = d.getMonthOfYear
      val day = d.getDayOfMonth
      val weekOfMonth = (day - 1) / 7 + 1
      month match {
        case JANUARY =>
          if (day == 1) Some("元日")
          else if (year >= 2000) weekOfMonth == 2 && (d is Monday) opt "成人の日"
          else day == 15 opt "成人の日"
        case FEBRUARY =>
          if (day == 11) year >= 1967 opt "建国記念の日"
          else d == 昭和天皇大喪の礼 opt "昭和天皇の大喪の礼"
        case MARCH =>
          day == springEquinox(year) opt "春分の日"
        case APRIL =>
          if (day == 29) {
            if (year >= 2007) Some("昭和の日")
            else (year >= 1989) ? Some("みどりの日") | Some("天皇誕生日")
          } else {
            d == 明仁親王の結婚の儀 opt "皇太子明仁親王の結婚の儀"
          }
        case MAY => day match {
          case 3 => Some("憲法記念日")
          case 4 =>
            if (year >= 2007) Some("みどりの日")
            else year >= 1986 && (d is Monday) opt "国民の休日"
          case 5 => Some("こどもの日")
          case 6 if year >= 2007 && ((d is Tuesday) || (d is Wednesday)) => Some("振替休日")
          case _ => None
        }
        case JUNE =>
          d == 徳仁親王の結婚の儀 opt "皇太子徳仁親王の結婚の儀"
        case JULY =>
          if (year >= 2003) (weekOfMonth == 3) && (d is Monday) opt "海の日"
          else (year >= 1996) && (day == 20) opt "海の日"
        case AUGUST =>
          None
        case SEPTEMBER =>
          val equinox = autumnEquinox(year)
          if (day == equinox) {
            Some("秋分の日")
          } else if (year >= 2003) {
            if (weekOfMonth == 3 && (d is Monday)) Some("敬老の日")
            else (d is Tuesday) && day == (equinox - 1) opt "国民の休日"
          } else {
            year == 1966 && day == 15 opt "敬老の日"
          }
        case OCTOBER =>
          if (year >= 2000) (weekOfMonth == 2) && (d is Monday) opt "体育の日"
          else (year >= 1966) && (day == 10) opt "体育の日"
        case NOVEMBER =>
          if (day == 3) Some("文化の日")
          else if (day == 23) Some("勤労感謝の日")
          else d == 即位礼正殿の儀 opt "即位礼正殿の儀"
        case DECEMBER =>
          day == 23 && year >= 1989 opt "天皇誕生日"
      }
    }
    def substitute(d: LocalDate): Option[String] = {
      if ((d is Monday) && (d >= 振替休日施行) && holidayName(d - 1.day).isDefined) Some("振替休日")
      else None
    }
    holidayName(target) orElse substitute(target)
  }
  
  def unapply(d: LocalDate): Option[String] = apply(d)

  def unapply(d: DateTime): Option[String] = apply(d.toLocalDate)

}
