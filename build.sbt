

val gc          = TaskKey[Unit]("gc", "runs garbage collector")
lazy val gcTask = gc := {
  println("requesting garbage collection")
  System gc ()
}

def TargetScalaVersion = "2.13.14"

ThisBuild / crossScalaVersions := Seq("2.13.14", TargetScalaVersion)
ThisBuild / scalaVersion       := (ThisBuild / crossScalaVersions).value.last

lazy val root = project
  .in(file("."))
  .settings(
    /* basic project info */
    name                            := "ficus",
    description                     := "A Scala-friendly wrapper companion for Typesafe config",
    startYear                       := Some(2013),
    scalacOptions ++= Seq(
      "-language:implicitConversions",
      "-feature",
      "-deprecation",
      "-unchecked",
      "-encoding",
      "UTF-8",
      "-target:jvm-1." + {
        CrossVersion
          .partialVersion(scalaVersion.value)
          .collect {
            case (2, minor) if minor <= 10 & scalaVersion.value == "2.10.7" => "8"
            case (2, minor) if minor <= 10                                  => "7"
          }
          .getOrElse("8")
      }
    ) ++ (if (scalaVersion.value.startsWith("2.11") || scalaVersion.value.startsWith("2.10"))
            Seq("-Yclosure-elim", "-Yinline")
          else Seq.empty[String]),
    javacOptions ++= Seq(
      "-Xlint:unchecked",
      "-Xlint:deprecation"
    ),
    Compile / unmanagedSourceDirectories ++= {
      (Compile / unmanagedSourceDirectories).value.map { dir =>
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, 13) | (3, _)) => file(dir.getPath ++ "-2.13+")
          case _                      => file(dir.getPath ++ "-2.13-")
        }
      }
    },
    Test / unmanagedSourceDirectories ++= {
      (Test / unmanagedSourceDirectories).value.map { dir =>
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, 13) | (3, _)) => file(dir.getPath ++ "-2.13+")
          case _                      => file(dir.getPath ++ "-2.13-")
        }
      }
    },
    libraryDependencies ++= {
      if (scalaBinaryVersion.value != "3") {
        Seq(
          "com.chuusai"   %% "shapeless"      % "2.3.12"            % Test,
          "org.scala-lang" % "scala-reflect"  % scalaVersion.value % Provided,
          "org.scala-lang" % "scala-compiler" % scalaVersion.value % Provided
        )
      } else {
        Nil
      }
    },
    libraryDependencies ++=
      (if (Set("2.13").contains(scalaBinaryVersion.value))
         Seq("org.specs2" %% "specs2-core" % "4.8.3" % Test, "org.specs2" %% "specs2-scalacheck" % "4.8.3" % Test)
       else
         Seq("org.specs2" %% "specs2-core" % "5.0.0" % Test, "org.specs2" %% "specs2-scalacheck" % "5.0.0" % Test)) ++
         Seq("com.typesafe" % "config" % "1.3.4"),
    resolvers ++= Seq(
      Resolver.bintrayRepo("iheartradio", "maven"),
      Resolver.jcenterRepo
    ) ++ Resolver.sonatypeOssRepos("snapshots"),
    Test / parallelExecution        := true,
    /* sbt behavior */
    compile / logLevel              := Level.Warn,
    traceLevel                      := 5,
    offline                         := false,
    Compile / packageBin / mappings := {
      val ms = (Compile / packageBin / mappings).value
      ms filter { case (_, toPath) =>
        toPath != "application.conf"
      }
    },
    Publish.settings,
    releaseCrossBuild               := true,
    gcTask
  )
