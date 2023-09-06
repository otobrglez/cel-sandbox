val catsEffectVersion = "3.5.1"
val celVersion        = "0.2.0"
val http4sVersion     = "1.0.0-M40"
val log4catsVersion   = "2.6.0"
val circeVersion      = "0.14.6"
val logbackVersion    = "1.4.11"
val scalaTagsVersion  = "0.12.0"

ThisBuild / scalaVersion := "3.3.0"
ThisBuild / mainClass    := Some("com.pinkstack.cel.sandbox.SandboxServerApp")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect"     % catsEffectVersion,
  "dev.cel"        % "cel"             % celVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.lihaoyi"   %% "scalatags"       % scalaTagsVersion
) ++ Seq(
  "org.http4s" %% "http4s-core",
  "org.http4s" %% "http4s-server",
  "org.http4s" %% "http4s-client",
  "org.http4s" %% "http4s-dsl",
  "org.http4s" %% "http4s-ember-server",
  "org.http4s" %% "http4s-ember-client"
).map(_ % http4sVersion) ++ Seq(
  "org.typelevel" %% "log4cats-core",
  "org.typelevel" %% "log4cats-slf4j"
).map(_ % log4catsVersion) ++ Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion) ++ Seq(
  "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test
)
