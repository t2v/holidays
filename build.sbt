import scala.xml.NodeSeq

val commonSettings = Seq(
  version := "5.1",
  organization := "jp.t2v",
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.12.1"),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions"),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  ),
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
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
  crossScalaVersions := Seq("2.12.1"),
  publishMavenStyle := true,
  publish           := { },
  publishArtifact   := false,
  packagedArtifacts := Map.empty,
  publishTo         := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"),
  pomExtra          := NodeSeq.Empty
)

lazy val core = (project in file("core")).settings(commonSettings).settings(
  name := "holidays",
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
)

lazy val joda = (project in file("joda")).settings(commonSettings).settings(
  name := "holidays-joda",
  libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "2.9.7"
  ),
  publishMavenStyle := true,
  publish           := { },
  publishArtifact   := false,
  packagedArtifacts := Map.empty,
  publishTo         := Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"),
  pomExtra          := NodeSeq.Empty
).dependsOn(core)




