lazy val conduit = project
  .in(file("."))
  .settings(
    scalaVersion := "2.12.6",
    libraryDependencies := List(
      "org.typelevel"      %% "cats-free"           % "1.1.0",
      "org.http4s"         %% "http4s-dsl"          % "0.18.12",
      "org.http4s"         %% "http4s-circe"        % "0.18.12",
      "org.http4s"         %% "http4s-blaze-server" % "0.18.12",
      "io.circe"           %% "circe-derivation"    % "0.9.0-M3",
      "com.outr"           %% "scribe-slf4j"        % "2.4.0",
      "io.github.jmcardon" %% "tsec-http4s"         % "0.0.1-M11",
      "io.scalaland"       %% "chimney"             % "0.2.0",
      "io.estatico"        %% "newtype"             % "0.4.0",
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7"),
    addCompilerPlugin(MetalsPlugin.semanticdbScalac),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4"),
    scalacOptions += "-Yrangepos",
  )
