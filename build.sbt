import play.PlayScala

name := """kytong"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "angularjs" % "1.3.8",
  "org.webjars" % "angular-ui-bootstrap" % "0.12.0",
  "org.mockito" % "mockito-core" % "1.10.17" % "test",
  "org.webjars" % "bootswatch-united" % "3.1.1",
  "org.webjars" % "html5shiv" % "3.7.0",
  "org.webjars" % "respond" % "1.4.2"

)

lazy val root = project.in(file(".")).enablePlugins(PlayScala)
