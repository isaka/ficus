resolvers ++= Seq(
  "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/",
  "Scalafmt" at "https://mvnrepository.com/artifact/org.scalameta/scalafmt-dynamic"
)

addSbtPlugin("org.scoverage"  % "sbt-scoverage"      % "1.8.2")
//CVE-2023-4759 addSbtPlugin("org.scoverage"  % "sbt-coveralls"      % "1.3.1")
addSbtPlugin("com.github.sbt" % "sbt-release"        % "1.1.0")
//CVE-2023-46122 addSbtPlugin("com.typesafe"   % "sbt-mima-plugin"    % "0.9.2")
//addSbtPlugin("org.xerial.sbt" % "sbt-sonatype"       % "3.9.7")
//addSbtPlugin("com.github.sbt" % "sbt-pgp"            % "2.1.2")
//CVE-2022-1471 addSbtPlugin("com.codecommit" % "sbt-github-actions" % "0.12.0")
//addSbtPlugin("org.scalameta" %% "scalafmt-dynamic" % "3.8.2")
