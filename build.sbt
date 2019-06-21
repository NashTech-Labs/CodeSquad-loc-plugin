sbtPlugin := true

name := "stats"

organization := "io.github.knoldus"

version := "0.1.10"

crossScalaVersions <<= sbtVersion { ver =>
  ver match {
    case "0.12.4" => Seq("2.9.0", "2.9.1", "2.9.2", "2.9.3", "2.10.2")
    case "0.13.0" => Seq("2.10.2")
    case _ => sys.error("Unknown sbt version")
  }
}

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/knoldus/CodeSquad-loc-plugin</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:knoldus/CodeSquad-loc-plugin.git</url>
    <connection>scm:git:git@github.com:knoldus/CodeSquad-loc-plugin.git</connection>
  </scm>
  <developers>
    <developer>
      <id>randhir1910</id>
      <name>Randhir Kumar</name>
      <url>https://github.com/knoldus/CodeSquad-loc-plugin</url>
    </developer>
  </developers>
)
