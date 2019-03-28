ThisBuild / resolvers ++= Seq(
    "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
    Resolver.mavenLocal
)

name := "predict-xgboost-flink"

version := "0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"

val flinkVersion = "1.7.2"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion ,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion
)

libraryDependencies += "ml.dmlc" % "xgboost4j" % "0.82"
libraryDependencies += "org.apache.kafka" %% "kafka" % "1.1.1"
libraryDependencies += "org.apache.flink" %% "flink-connector-kafka" % "0.10.0"

val testDependencies = Seq(
  "net.manub" %% "scalatest-embedded-kafka" % "2.0.0" % "test",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies,
    libraryDependencies ++= testDependencies
  )

// make run command include the provided dependencies
Compile / run  := Defaults.runTask(Compile / fullClasspath,
                                   Compile / run / mainClass,
                                   Compile / run / runner
                                  ).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption  := (assembly / assemblyOption).value.copy(includeScala = false)
