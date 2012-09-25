name := "holidays"

version := "1.0"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-time" % "0.6",
  "org.scalatest" %% "scalatest" % "1.8" % "test"
)

organization := "jp.t2v"

publishTo := sys.env.get("LOCAL_MAVEN_REPO").map { dir =>
  Resolver.file("maven-repo", file(dir))(Patterns(true, Resolver.mavenStyleBasePattern))
}
