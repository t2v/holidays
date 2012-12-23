name := "holidays"

version := "2.0"

crossScalaVersions := Seq("2.9.1", "2.9.2", "2.10.0")

scalacOptions <++= scalaVersion map { v =>
  if (v.startsWith("2.10"))
    Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions", "-language:reflectiveCalls")
  else
    Seq("-unchecked", "-deprecation")
}

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "0.2.0" cross CrossVersion.full,
  "org.scalatest" %% "scalatest" % "1.8" % "test" cross CrossVersion.full
)

organization := "jp.t2v"

publishTo := sys.env.get("LOCAL_MAVEN_REPO").map { dir =>
  Resolver.file("maven-repo", file(dir))(Patterns(true, Resolver.mavenStyleBasePattern))
}
