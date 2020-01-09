name := "testApachePoi"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.apache.poi" % "poi" % "4.1.1"
libraryDependencies += "org.apache.poi" % "poi-ooxml" % "4.1.1"

val elastic4sVersion = "7.3.1"
libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-json-circe" % elastic4sVersion,
)