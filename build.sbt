name := "holidays"

version := "3.0"

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.14.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

organization := "jp.t2v"

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) 
    Some("snapshots" at nexus + "content/repositories/snapshots") 
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
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
