//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
//_/
//_/  CopyRight(C) K.Tsunoda(AddinBox) 2001 All Rights Reserved.
//_/  ( AddinBox  http://addinbox.sakura.ne.jp/index.htm )
//_/  (  旧サイト  http://www.h3.dion.ne.jp/~sakatsu/index.htm )
//_/
//_/    この祝日マクロは『kt関数アドイン』で使用しているものです。
//_/    このロジックは、レスポンスを第一義として、可能な限り少ない
//_/    【条件判定の実行】で結果を出せるように設計してあります。
//_/
//_/    この関数では以下の祝日変更までサポートしています。
//_/    (a) 2019年施行の「天皇誕生日の変更」 12/23⇒2/23 (補：2019年には[天皇誕生日]はありません)
//_/    (b) 2019年の徳仁親王の即位日(5/1) および
//_/       祝日に挟まれて「国民の休日」となる 4/30(平成天皇の退位日) ＆ 5/2 の２休日
//_/    (c) 2019年の「即位の礼 正殿の儀 (10/22) 」
//_/    (d) 2020年施行の「体育の日の改名」⇒スポーツの日
//_/    (e) 五輪特措法による2020年の「祝日移動」
//_/       海の日：7/20(3rd Mon)⇒7/23, スポーツの日:10/12(2nd Mon)⇒7/24, 山の日：8/11⇒8/10
//_/
//_/  (*1)このマクロを引用するに当たっては、必ずこのコメントも
//_/      一緒に引用する事とします。
//_/  (*2)他サイト上で本マクロを直接引用する事は、ご遠慮願います。
//_/      【 http://addinbox.sakura.ne.jp/holiday_logic.htm 】
//_/      へのリンクによる紹介で対応して下さい。
//_/  (*3)[ktHolidayName]という関数名そのものは、各自の環境に
//_/      おける命名規則に沿って変更しても構いません。
//_/
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
package jp.t2v.util.locale

import java.time.{DayOfWeek, LocalDate}
import java.time.Month._
import java.time.DayOfWeek.{MONDAY, TUESDAY, WEDNESDAY}

import scala.annotation.switch
import scala.math.Ordering.Implicits._

object Holidays extends (LocalDate => Option[String]) {

  private val 祝日法施行             = LocalDate.of(1948, JULY,     20)
  private val 明仁親王の結婚の儀     = LocalDate.of(1959, APRIL,    10)
  private val 昭和天皇大喪の礼       = LocalDate.of(1989, FEBRUARY, 24)
  private val 徳仁親王の結婚の儀     = LocalDate.of(1993, JUNE,     9)
  private val 即位礼正殿の儀         = LocalDate.of(1990, NOVEMBER, 12)
  private val 振替休日施行           = LocalDate.of(1973, APRIL,    12)
  private val 平成天皇の退位         = LocalDate.of(2019, APRIL, 30)
  private val 徳仁親王の即位         = LocalDate.of(2019, MAY, 1)
  private val 国民の休日2019GW       = LocalDate.of(2019, MAY, 2)
  private val 即位礼正殿の儀_徳仁親王 = LocalDate.of(2019, OCTOBER, 22)

  private[this] implicit class LocalDateWrapper(val d: LocalDate) extends AnyVal {
    @inline def is(w: DayOfWeek): Boolean = d.getDayOfWeek == w
  }
  
  private[this] class Condition[X](b: Boolean, s: => X) {
    def |[Y >: X](n: => Y): Y = if (b) s else n
  }
  private[this] implicit class BooleanWrapper(val b: Boolean) extends AnyVal {
    def ?[X](s: => X) = new Condition(b, s)
    @inline def opt(s: String): Option[String] = if (b) Some(s) else None
  }
  
  private[this] def springEquinox(year: Int): Int = {
    if (year <= 1947) return 99
    if (year <= 1979) return (20.8357 + (0.242194 * (year - 1980)) - ((year - 1983) / 4)).toInt
    if (year <= 2099) return (20.8431 + (0.242194 * (year - 1980)) - ((year - 1980) / 4)).toInt
    if (year <= 2150) return (21.851  + (0.242194 * (year - 1980)) - ((year - 1980) / 4)).toInt
    99
  }

  private[this] def autumnEquinox(year: Int): Int = {
    if (year <= 1947) return 99
    if (year <= 1979) return (23.2588 + (0.242194 * (year - 1980)) - ((year - 1983) / 4)).toInt
    if (year <= 2099) return (23.2488 + (0.242194 * (year - 1980)) - ((year - 1980) / 4)).toInt
    if (year <= 2150) return (24.2488 + (0.242194 * (year - 1980)) - ((year - 1980) / 4)).toInt
    99
  }

  private[this] implicit val localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)

  def apply(target: LocalDate): Option[String] = {
    def holidayName(d: LocalDate): Option[String] = {
      if (d < 祝日法施行) return None
      val year = d.getYear
      val month = d.getMonth
      val day = d.getDayOfMonth
      val weekOfMonth = (day - 1) / 7 + 1
      month match {
        case JANUARY =>
          if      (day == 1) Some("元日")
          else if (year >= 2000) weekOfMonth == 2 && (d is MONDAY) opt "成人の日"
          else    day == 15 opt "成人の日"
        case FEBRUARY =>
          if      (day == 11) year >= 1967 opt "建国記念の日"
          else if (day == 23) year >= 2020 opt "天皇誕生日"
          else d == 昭和天皇大喪の礼 opt "昭和天皇の大喪の礼"
        case MARCH =>
          day == springEquinox(year) opt "春分の日"
        case APRIL =>
          if (day == 29) {
            if (year >= 2007) Some("昭和の日")
            else (year >= 1989) ? Some("みどりの日") | Some("天皇誕生日")
          }
          else if (d == 平成天皇の退位) Some("国民の休日")
          else     d == 明仁親王の結婚の儀 opt "皇太子明仁親王の結婚の儀"
        case MAY => (day: @switch) match {
          case 3 => Some("憲法記念日")
          case 4 =>
            if (year >= 2007) Some("みどりの日")
            else year >= 1986 && (d is MONDAY) opt "国民の休日"
          case 5 => Some("こどもの日")
          case 6 if year >= 2007 && ((d is TUESDAY) || (d is WEDNESDAY)) => Some("振替休日")
          case _ =>
            if      (d == 徳仁親王の即位) Some("即位の日")
            else if (d == 国民の休日2019GW) Some("国民の休日")
            else None
        }
        case JUNE =>
          d == 徳仁親王の結婚の儀 opt "皇太子徳仁親王の結婚の儀"
        case JULY =>
          if      (year >= 2021) (weekOfMonth == 3) && (d is MONDAY) opt "海の日"
          else if (year == 2020) (day: @switch) match {
                                    case 23 => Some("海の日")
                                    case 24 => Some("スポーツの日")
                                    case _  => None
                                 }
          else if (year >= 2003) (weekOfMonth == 3) && (d is MONDAY) opt "海の日"
          else if (year >= 1996) (day == 20) opt "海の日"
          else None
        case AUGUST =>
          if      (year >= 2021) (day == 11) opt "山の日"
          else if (year == 2020) (day == 10) opt "山の日"
          else if (year >= 2016) (day == 11) opt "山の日"
          else None
        case SEPTEMBER =>
          val equinox = autumnEquinox(year)
          if (day == equinox) {
            Some("秋分の日")
          } else if (year >= 2003) {
            if (weekOfMonth == 3 && (d is MONDAY)) Some("敬老の日")
            else (d is TUESDAY) && day == (equinox - 1) opt "国民の休日"
          } else {
            year == 1966 && day == 15 opt "敬老の日"
          }
        case OCTOBER =>
          if      (year >= 2021) (weekOfMonth == 2) && (d is MONDAY) opt "スポーツの日"
          else if (year == 2020) None
          else if (year >= 2000) {
            if ((weekOfMonth == 2) && (d is MONDAY)) Some("体育の日")
            else d == 即位礼正殿の儀_徳仁親王 opt "即位礼正殿の儀"
          }
          else    (year >= 1966) && (day == 10) opt "体育の日"
        case NOVEMBER =>
          if      (day == 3) Some("文化の日")
          else if (day == 23) Some("勤労感謝の日")
          else d == 即位礼正殿の儀 opt "即位礼正殿の儀"
        case DECEMBER =>
          (day == 23) && (year >= 1989) && (year <= 2018) opt "天皇誕生日"
      }
    }
    def substitute(d: LocalDate): Option[String] = {
      ((d is MONDAY) && (d >= 振替休日施行) && holidayName(d.minusDays(1)).isDefined) opt "振替休日"
    }
    holidayName(target) orElse substitute(target)
  }

  def unapply[A: LocalDateConverter](value: A): Option[String] = apply(implicitly[LocalDateConverter[A]].apply(value))

}
