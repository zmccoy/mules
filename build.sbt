lazy val mules = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .settings(skip in publish := true)
  .settings(commonSettings)
  .aggregate(core, caffeine, reload, noop, bench)

lazy val bench = project.in(file("modules/bench"))
  .disablePlugins(MimaPlugin)
  .enablePlugins(JmhPlugin)
  .settings(skip in publish := true)
  .settings(commonSettings)
  .dependsOn(core, caffeine)

lazy val core = project.in(file("modules/core"))
  .settings(commonSettings)
  .settings(
    name := "mules"
  )

lazy val caffeine = project.in(file("modules/caffeine"))
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "mules-caffeine",
    libraryDependencies ++= Seq(
      "com.github.ben-manes.caffeine" % "caffeine" % "2.8.8"
    )
  )

lazy val noop = project.in(file("modules/noop"))
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "mules-noop"
  )

lazy val reload = project.in(file("modules/reload"))
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "mules-reload",
    libraryDependencies ++= Seq(
      "org.typelevel"               %% "cats-collections-core"      % catsCollectionV
    )
  )

val catsV = "2.3.1"
val catsEffectV = "3.0.0-RC1"
val catsCollectionV = "0.9.1"

val specs2V = "4.10.6"
val disciplineSpecs2V = "1.0.0"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.4",
  crossScalaVersions := Seq(scalaVersion.value, "2.12.10"),

  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),

  libraryDependencies ++= Seq(
    "org.typelevel"               %% "cats-core"                  % catsV,
    "org.typelevel"               %% "cats-effect"                % catsEffectV,
    "io.chrisdavenport"           %% "mapref"                     % "0.1.1",

    "org.typelevel"               %% "cats-effect-laws"           % catsEffectV   % Test,
    "com.codecommit"              %% "cats-effect-testing-specs2" % "0.4.2"       % Test,
    "org.specs2"                  %% "specs2-core"                % specs2V       % Test,
    "org.specs2"                  %% "specs2-scalacheck"          % specs2V       % Test,
    "org.typelevel"               %% "discipline-specs2"          % disciplineSpecs2V % Test,
  )
)

inThisBuild(List(
  organization := "io.chrisdavenport",
  homepage := Some(url("https://github.com/ChristopherDavenport/mules")),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  developers := List(
    Developer(
      "ChristopherDavenport",
      "Christopher Davenport",
      "chris@christopherdavenport.tech",
      url("https://github.com/ChristopherDavenport")
    )
  ),
  scalacOptions in (Compile, doc) ++= Seq(
      "-groups",
      "-sourcepath", (baseDirectory in LocalRootProject).value.getAbsolutePath,
      "-doc-source-url", "https://github.com/ChristopherDavenport/mules/blob/v" + version.value + "€{FILE_PATH}.scala"
  ),
))
