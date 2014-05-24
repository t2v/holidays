name := "holidays"

version := "3.0"

crossScalaVersions := Seq("2.9.1", "2.9.2", "2.9.3", "2.10.0", "2.11.0")

scalacOptions <++= scalaVersion map { v =>
  if (v.startsWith("2.9."))
    Seq("-unchecked", "-deprecation")
  else
    Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions", "-language:reflectiveCalls")
}

libraryDependencies <<= scalaVersion { v => Seq(
  "com.github.nscala-time" %% "nscala-time" % "1.0.0",
  if (v.startsWith("2.9."))
    "org.scalatest" %% "scalatest" % "1.9.2" % "test" cross CrossVersion.full
  else
    "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)}

organization := "jp.t2v"

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) 
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