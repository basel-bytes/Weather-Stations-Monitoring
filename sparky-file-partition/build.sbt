ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "sparky-file-partition"
  )

libraryDependencies ++= Seq(
                          "org.apache.spark" %% "spark-core" % "3.3.2",
                          "org.apache.spark" %% "spark-sql" % "3.3.2"
)
libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "3.3.5" % Test
