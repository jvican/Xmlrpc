name := "xmlrpc"

version := "1.2.2-SNAPSHOT"

description := "Module that gives full compatibility with XML-RPC for Scala"

organization := "com.github.jvican"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.11.8", "2.12.8")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= {
  val scalazVersion = "7.2.27"
  val akkaHttp = "10.1.8"
  val scalaTestVersion = "3.0.7"
  val shapelessVersion = "2.3.3"

  Seq(
    "org.scalaz"             %% "scalaz-core"    % scalazVersion,
    "com.typesafe.akka"      %% "akka-http-xml"  % akkaHttp,
    "com.typesafe.akka"      %% "akka-actor"     % "2.5.23",
    "com.typesafe.akka"      %% "akka-stream"    % "2.5.23",
    "org.scalatest"          %% "scalatest"      % scalaTestVersion    % "test",
    "com.chuusai"            %% "shapeless"      % shapelessVersion
  )
}

// Settings to publish to Sonatype
licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/MIT"))

pomExtra := <url>https://github.com/jvican/xmlrpc</url>
  <scm>
    <url>https://github.com/jvican/xmlrpc.git</url>
    <connection>scm:git:git@github.com:jvican/xmlrpc.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jvican</id>
      <name>Jorge Vicente Cantero</name>
      <url>https://github.com/jvican</url>
    </developer>
  </developers>
