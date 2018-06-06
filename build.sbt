lazy val V = new {
  val doobie = "0.5.3"
  val http4s = "0.18.12"
}

lazy val conduit = project
  .in(file("."))
  .settings(
    scalaVersion := "2.12.4",
    libraryDependencies := List(
      "com.outr"           %% "scribe-slf4j"        % "2.5.0",
      "eu.timepit"         %% "refined"             % "0.9.0",
      "io.circe"           %% "circe-derivation"    % "0.9.0-M3",
      "io.circe"           %% "circe-refined"       % "0.9.3",
      "io.estatico"        %% "newtype"             % "0.4.0",
      "io.github.jmcardon" %% "tsec-http4s"         % "0.0.1-M11",
      "io.scalaland"       %% "chimney"             % "0.2.0",
      "org.http4s"         %% "http4s-blaze-server" % V.http4s,
      "org.http4s"         %% "http4s-circe"        % V.http4s,
      "org.http4s"         %% "http4s-dsl"          % V.http4s,
      "org.tpolecat"       %% "doobie-core"         % V.doobie,
      "org.tpolecat"       %% "doobie-postgres"     % V.doobie,
      "org.tpolecat"       %% "doobie-specs2"       % V.doobie,
      "org.typelevel"      %% "cats-free"           % "1.1.0",
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7"),
    addCompilerPlugin(MetalsPlugin.semanticdbScalac),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4"),
    scalacOptions += "-Yrangepos",
  )
