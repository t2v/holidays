# 祝日計算 in Scala [![Build Status](https://travis-ci.org/t2v/holidays.svg?branch=master)](https://travis-ci.org/t2v/holidays)

http://www.h3.dion.ne.jp/~sakatsu/holiday_logic.htm の Scala 移植版です。

※山の日にも対応しました。


# 導入


## Java8 Date&Time API と共に使いたい場合

以下の記述を `build.sbt` に足してください。
Scala 2.12.x に対応しています。

```scala
libraryDependencies += "jp.t2v" %% "holidays" % "5.1"
```

## Joda-Time と共に使いたい場合

### Holidays5.x 以降

Holidaysの依存に [nscala-time](https://github.com/nscala-time/nscala-time) が含まれなくなったため、個別に nscala-time か joda-time を依存性に追加してください。
Scala 2.12.x に対応しています。

```scala
libraryDependencies += "jp.t2v" %% "holidays" % "5.1"
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.14.0"
```

### Holidays4.x 以前

以下の記述を `build.sbt` に足してください。
Scala2.9.1, 2.9.2, 2.9.3, 2.10.x, 2.11.x, 2.12.x に対応しています。

```scala
// Scala2.10.x ～ Scala2.12.x
libraryDependencies += "jp.t2v" %% "holidays" % "4.0"

// Scala2.9.x
libraryDependencies += "jp.t2v" %% "holidays" % "3.0"
```

# 使い方

## Implicit に使う

### Java8 Date&Time API

```scala
import java.time.LocalDate
import jp.t2v.util.locale.Implicits._

LocalDate.now().holidayName   // Option[String] で休日名
LocalDate.now().isHoliday     // Boolean

LocalDate.of(2012, 9, 22).holidayName // Some("秋分の日")
LocalDate.of(2009, 5,  6).holidayName // Some("振替休日")
LocalDate.of(2012, 9, 10).holidayName // None
```

`LocalDate` の他に `LocalDateTime` および `ZonedDateTime` をサポートしています。

### Joda-Time(with nscala-time)

```scala
import com.github.nscala_time.time.Imports._
import jp.t2v.util.locale.Implicits._

DateTime.now.holidayName   // Option[String] で休日名
DateTime.now.isHoliday     // Boolean

new LocalDate(2012, 9, 22).holidayName // Some("秋分の日")
new LocalDate(2009, 5,  6).holidayName // Some("振替休日")
new LocalDate(2012, 9, 10).holidayName // None
```

`LocalDate` の他に `DateTime` をサポートしています。

## Explicit に使う

### Java8 Date&Time API

```scala
import java.time.LocalDate
import jp.t2v.util.locale.Holidays

Holidays(LocalDate.of(2012, 9, 22))  // Some("秋分の日")
Holidays(LocalDate.of(2009, 5,  6))  // Some("振替休日")
Holidays(LocalDate.of(2012, 9, 10))  // None
```

### Joda-Time(with nscala-time)

```scala
import com.github.nscala_time.time.Imports._
import jp.t2v.util.locale.Holidays

Holidays(new LocalDate(2012, 9, 22))  // Some("秋分の日")
Holidays(new LocalDate(2009, 5,  6))  // Some("振替休日")
Holidays(new LocalDate(2012, 9, 10))  // None
```

`LocalDate` の他に `DateTime` をサポートしています。

## パターンマッチにも

```scala
import java.time.LocalDate
import jp.t2v.util.locale.Holidays

val start = LocalDate.of(2012, 4, 28)
val end = LocalDate.of(2012, 5, 6)
val days: Seq[LocalDate] = Stream.iterate(start)(_.plusDays(1)).takeWhile(end >)

val names = days.map {
  case Holidays(name) => name
  case _              => "平日"
}

names == Seq("平日", "昭和の日", "振替休日", "平日", "平日", "憲法記念日", "みどりの日", "こどもの日")
```

Java8 `LocalDate`, `LocalDateTime`, `ZonedDateTime` および Joda-Time `LocalDate`, `DateTime` をサポートしています。

# Copyright

```
'_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
'_/
'_/　CopyRight(C) K.Tsunoda(AddinBox) 2001 All Rights Reserved.
'_/　( http://www.h3.dion.ne.jp/~sakatsu/index.htm )
'_/
'_/　　この祝日マクロは『kt関数アドイン』で使用しているものです。
'_/　　このロジックは、レスポンスを第一義として、可能な限り少ない
'_/　  【条件判定の実行】で結果を出せるように設計してあります。
'_/　　この関数では、２００７年施行の改正祝日法(昭和の日)までを
'_/　  サポートしています(９月の国民の休日を含む)。
'_/
'_/　(*1)このマクロを引用するに当たっては、必ずこのコメントも
'_/　　　一緒に引用する事とします。
'_/　(*2)他サイト上で本マクロを直接引用する事は、ご遠慮願います。
'_/　　　【 http://www.h3.dion.ne.jp/~sakatsu/holiday_logic.htm 】
'_/　　　へのリンクによる紹介で対応して下さい。
'_/　(*3)[ktHolidayName]という関数名そのものは、各自の環境に
'_/　　　おける命名規則に沿って変更しても構いません。
'_/　
'_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
```

