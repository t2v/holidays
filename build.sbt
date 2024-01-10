import scala.xml.NodeSeq

def jodaDependency = "joda-time" % "joda-time" % "2.12.6"

val commonSettings = Seq(
  version := "7.1",
  organization := "jp.t2v",
  scalaVersion := "3.3.1",
  crossScalaVersions := Seq("2.11.12", "2.12.18", "2.13.12", "3.3.1"),
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _)) => Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions"/*, "-Yexplicit-nulls"*/)
      case _            => Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions", "-Xdisable-assertions")
    }
  },
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.17" % "test"
  ),
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  Test / fork := true,
  Test / publishArtifact  := false,
  pomIncludeRepository := { _ => false },
  pomExtra :=
    <url>https://github.com/t2v/holidays</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:t2v/holidays.git</url>
      <connection>scm:git:git@github.com:t2v/holidays.git</connection>
    </scm>
    <developers>
      <developer>
        <id>gakuzzzz</id>
        <name>gakuzzzz</name>
        <url>https://github.com/gakuzzzz</url>
      </developer>
    </developers>
)

lazy val root = (project in file(".")).aggregate(core, joda).settings(
  scalaVersion       := "3.3.1",
  crossScalaVersions := Seq("2.11.12", "2.12.18", "2.13.12", "3.3.1"),
  publishMavenStyle  := true,
  publish            := { },
  publishArtifact    := false,
  packagedArtifacts  := Map.empty,
  publishTo          := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"),
  pomExtra           := NodeSeq.Empty
)

lazy val core = (project in file("core")).settings(commonSettings).settings(
  name := "holidays",
  Test / fullClasspath ~= { oldPath =>
    val newPath = oldPath.filterNot(_.data.toString.contains("joda-time"))
    assert((oldPath.size - newPath.size) == 1, (newPath, oldPath))
    newPath
  },
  libraryDependencies ++= Seq(
    jodaDependency % Provided
  )
)

lazy val joda = (project in file("joda")).settings(commonSettings).settings(
  name := "holidays-joda",
  libraryDependencies ++= Seq(
    jodaDependency % Test
  ),
  publishMavenStyle := true,
  publish           := { },
  publishArtifact   := false,
  packagedArtifacts := Map.empty,
  publishTo         := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"),
  pomExtra          := NodeSeq.Empty
).dependsOn(core)

