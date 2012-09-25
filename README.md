# 祝日計算 in Scala [![Build Status](https://secure.travis-ci.org/t2v/holidays.png)](http://travis-ci.org/t2v/holidays)

http://www.h3.dion.ne.jp/~sakatsu/index.htm の Scala 移植版です。

# 依存ライブラリ

- scala-time

# 導入

以下の記述を `build.sbt` か `Build.scala` に足してください。

```scala
resolvers += "t2v.jp repo" at "http://www.t2v.jp/maven-repo/"
```

```scala
libraryDependencies += "jp.t2v" % "holidays_2.9.2" % "1.0"
```

# 使い方

## Implicit に使う

```scala
import org.scala_tools.time.Imports._
import jp.t2v.util.locale.Implicits._

DateTime.now.holidayName   // Option[String] で休日名
DateTime.now.isHoliday     // Boolean

new LocalDate(2012, 9, 22).holidayName // Some("秋分の日")
new LocalDate(2009, 5,  6).holidayName // Some("振替休日")
new LocalDate(2012, 9, 10).holidayName // None
```

## Explicit に使う

```scala
import org.scala_tools.time.Imports._
import jp.t2v.util.locale.Holidays

Holidays(new LocalDate(2012, 9, 22))  // Some("秋分の日")
Holidays(new LocalDate(2009, 5,  6))  // Some("振替休日")
Holidays(new LocalDate(2012, 9, 10))  // None
```

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

