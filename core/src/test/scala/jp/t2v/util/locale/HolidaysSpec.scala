package jp.t2v.util.locale

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalQueries

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HolidaysSpec extends AnyFlatSpec with Matchers {

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
    LocalDate.of(2019,  2, 23).holidayName should equal (None)
    LocalDate.of(2020,  2, 23).holidayName should equal (Some("天皇誕生日"))
    LocalDate.of(2020,  7, 23).holidayName should equal (Some("海の日"))
    LocalDate.of(2021,  7, 19).holidayName should equal (None)
    LocalDate.of(2021,  7, 22).holidayName should equal (Some("海の日"))
    LocalDate.of(2022,  7, 18).holidayName should equal (Some("海の日"))
    LocalDate.of(2020,  7, 24).holidayName should equal (Some("スポーツの日"))
    LocalDate.of(2021,  7, 23).holidayName should equal (Some("スポーツの日"))
    LocalDate.of(2020,  8, 10).holidayName should equal (Some("山の日"))
    LocalDate.of(2020,  8, 11).holidayName should equal (None)
    LocalDate.of(2021,  8,  8).holidayName should equal (Some("山の日"))
    LocalDate.of(2021,  8, 11).holidayName should equal (None)
    LocalDate.of(2022,  8, 11).holidayName should equal (Some("山の日"))
    LocalDate.of(2021, 10, 11).holidayName should equal (None)
    LocalDate.of(2022, 10, 10).holidayName should equal (Some("スポーツの日"))
    LocalDate.of(2020, 10, 12).holidayName should equal (None)
    LocalDate.of(2019, 10, 14).holidayName should equal (Some("体育の日"))
    LocalDate.of(2018, 12, 23).holidayName should equal (Some("天皇誕生日"))
    LocalDate.of(2019, 12, 23).holidayName should equal (None)
    LocalDate.of(1959,  4, 10).holidayName should equal (Some("皇太子明仁親王の結婚の儀"))
    LocalDate.of(2019,  4, 30).holidayName should equal (Some("国民の休日"))
    LocalDate.of(2019,  5,  1).holidayName should equal (Some("即位の日"))
    LocalDate.of(2019,  5,  2).holidayName should equal (Some("国民の休日"))
    LocalDate.of(2019, 10, 22).holidayName should equal (Some("即位礼正殿の儀"))
    LocalDate.of(2020,  5,  1).holidayName should equal (None)
    LocalDate.of(2020, 10, 22).holidayName should equal (None)

    // 水曜日の振替休日
    LocalDate.of(2009,  5,  6).holidayName should equal (Some("振替休日"))
  }

  implicit class WrapString(s: String) {
    def ld: LocalDate = DateTimeFormatter.ofPattern("yyyy/MM/dd").parse(s, TemporalQueries.localDate)
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

  "core test" should "not have joda dependency in classpath" in {
    assertThrows[NoClassDefFoundError](
      LocalDateConverter.jodaLocalDateConverter(null)
    )
    assertThrows[NoClassDefFoundError](
      LocalDateConverter.jodaLocalTimeConverter(null)
    )
  }
}
