lazy val commonSettings = Seq(
  organization := "me.eax",
  version := "0.1",
  scalaVersion := "2.11.7",
  scalacOptions ++= Seq("-Xmax-classfile-name", "100")
)

lazy val withTracing = (project in file("tracing")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
      "com.typesafe.akka" %% "akka-actor" % "2.3.12",
      "com.typesafe.akka" %% "akka-slf4j" % "2.3.12",
      "io.kamon" %% "kamon-core" % "0.5.0",
      "io.kamon" %% "kamon-scala" % "0.5.0",
      "io.kamon" %% "kamon-akka" % "0.5.0",
      "org.aspectj" % "aspectjweaver" % "1.8.2"
    )
  ).
  settings(javaOptions <++= AspectjKeys.weaverOptions in Aspectj).
  settings(fork in run := true).
  settings(aspectjSettings :_*)


lazy val root = (project in file(".")).
  aggregate(withTracing)

