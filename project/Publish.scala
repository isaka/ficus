import sbt.*
import sbt.Keys.*
import sbtrelease.ReleasePlugin.autoImport.*
import sbtrelease.ReleaseStateTransformations.*

import scala.xml.Elem

object Publish {

  private val developers: Elem = <developers>
    <developer>
      <id>ceedubs</id>
      <name>Cody Allen</name>
      <email>ceedubs@gmail.com</email>
    </developer>
    <developer>
      <id>kailuowang</id>
      <name>Kailuo Wang</name>
      <email>kailuo.wang@gmail.com</email>
    </developer>
  </developers>
  pomExtra in Global := {

    developers
  }

  val publishingSettings = Seq(
    ThisBuild / organization      := "com.mimozar",
    publishMavenStyle             := true,
    licenses                      := Seq("MIT" -> url("http://www.opensource.org/licenses/mit-license.html")),
    pomIncludeRepository          := { _ => false },
    Test / publishArtifact        := false,
    publishTo                     := Some("Local Repo" at "~/.m2/repository/releases"),
    pomExtra                      := developers,
    releaseCrossBuild             := true,
//    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseProcess                := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      setNextVersion,
      commitNextVersion,
      ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
      pushChanges
    )
  )

  val settings = publishingSettings
}
