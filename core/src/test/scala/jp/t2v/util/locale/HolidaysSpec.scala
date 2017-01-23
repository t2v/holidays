package jp.t2v.util.locale

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.scalatest._

class HolidaysSpec extends FlatSpec with Matchers {

  "The Holidays" should "return holiday name" in {
    import jp.t2v.util.locale.Implicits._

    LocalDate.of(2012,  1,  1).holidayName should equal (Some("元日"))
    LocalDate.of(2012,  1,  2).holidayName should equal (Some("振替休日"))
    LocalDate.of(2012,  1,  9).holidayName should equal (Some("成人の日"))
    LocalDate.of(2012,  2, 11).holidayName should equal (Some("建国記念の日"))
    LocalDate.of(2012,  3, 20).holidayName should equal (Some("春分の日"))
    LocalDate.of(2012,  4, 29).holidayName should equal (Some("昭和の日"))
    LocalDate.of(2012,  4, 30).holidayName should equal (Some("振替休日"))
    LocalDate.of(2012,  5,  3).holidayName should equal (Some("憲法記念日"))
    LocalDate.of(2012,  5,  4).holidayName should equal (Some("みどりの日"))
    LocalDate.of(2012,  5,  5).holidayName should equal (Some("こどもの日"))
    LocalDate.of(2012,  7, 16).holidayName should equal (Some("海の日"))
    LocalDate.of(2012,  9, 17).holidayName should equal (Some("敬老の日"))
    LocalDate.of(2012,  9, 22).holidayName should equal (Some("秋分の日"))
    LocalDate.of(2012, 10,  8).holidayName should equal (Some("体育の日"))
    LocalDate.of(2012, 11,  3).holidayName should equal (Some("文化の日"))
    LocalDate.of(2012, 11, 23).holidayName should equal (Some("勤労感謝の日"))
    LocalDate.of(2012, 12, 23).holidayName should equal (Some("天皇誕生日"))
    LocalDate.of(2012, 12, 24).holidayName should equal (Some("振替休日"))

    // 水曜日の振替休日
    LocalDate.of(2009,  5,  6).holidayName should equal (Some("振替休日"))
  }

  implicit class WrapString(s: String) {
    def ld: LocalDate = DateTimeFormatter.ofPattern("yyyy/MM/dd").parse(s, LocalDate.from)
  }

  "Holidays extractor" should "extract holiday name" in {
    val d = Seq(
      "2012/04/28".ld,
      "2012/04/29".ld,
      "2012/04/30".ld,
      "2012/05/01".ld,
      "2012/05/02".ld,
      "2012/05/03".ld,
      "2012/05/04".ld,
      "2012/05/05".ld,
      "2012/05/06".ld
    )

    val actual = d map {
      case Holidays(name) => name
      case _              => "平日"
    }
    
    val expected = Seq(
      "平日",
      "昭和の日",
      "振替休日",
      "平日",
      "平日",
      "憲法記念日",
      "みどりの日",
      "こどもの日",
      "平日"
    )

    actual should equal (expected)
  }

}
