# play-actuator

[![CI](https://github.com/felipebonezi/play-actuator/actions/workflows/continouos-integration.yml/badge.svg)](https://github.com/felipebonezi/play-actuator/actions/workflows/continouos-integration.yml)
[![Renovate](https://img.shields.io/badge/renovate-enabled-brightgreen.svg)](https://renovatebot.com)
[![Licence](https://img.shields.io/github/license/felipebonezi/play-actuator?color=blue)](https://github.com/felipebonezi/play-actuator/blob/main/LICENSE)

Play! Framework plugin with actuator routes that gives you infos about application uptime.
You may consider some indicators to define what is `Up` or `Down` for your (check more info at `Health endpoint details`).

This project is inspired by `Spring Boot Actuator` architecture
([Check this post to know more about it](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator))
.

## How to use?
[![Version](https://img.shields.io/github/v/release/felipebonezi/play-actuator?logo=java)](https://github.com/felipebonezi/play-actuator/releases)

You can import as a project dependency in your `build.sbt` file. Pick the
suffix that matches the Play major your application is on (see
[Cross-building](#cross-building) below).

```sbt
  // Play 2.9 (existing, unsuffixed coordinates - no migration needed)
  libraryDependencies += "io.github.felipebonezi" %% "play-actuator" % "(version)"

  // Play 3.0 (new)
  libraryDependencies += "io.github.felipebonezi" %% "play-actuator_play30" % "(version)"
```

After that, you need to configure `play.actuator.ActuatorRouter` into project `conf/routes` file.

```scala
->      /actuator      play.actuator.ActuatorRouter
```

Run your project and check Actuators endpoints - e.g. `/actuator/health`.

## Actuator endpoints

Below we have all project endpoints available on this project.

| Endpoint ID | Description                                  | Path                | Ready to use? |
|-------------|----------------------------------------------|---------------------|---------------|
| health      | Displays your application’s health status.   | `/actuator/health`  | ✔️            |
| info        | Displays information about your application. | `/actuator/info`    | ✔️            |
| logfile     | Returns the contents of the log file.        | `/actuator/logfile` | ✖️            |

## Health endpoint details

You can check all health indicators by simple activating each configuration.

### Disk Space Indicator

Show to you the total, free and usable disk space.

**No need of any extra dependency, it's under the hood.**

`play.actuator.health.indicators.diskSpace = true`

### Database Indicator

Show to you information about your database using JDBC or Slick connection.

`play.actuator.health.indicators.database = true`

It depends on which dependency indicator you'll use.
**You need to choose only one dependency to work with!**

#### JDBC
```sbt
  // Play 2.9
  libraryDependencies += "io.github.felipebonezi" %% "play-actuator-jdbc-indicator" % "(version)"
  // Play 3.0
  libraryDependencies += "io.github.felipebonezi" %% "play-actuator-jdbc-indicator_play30" % "(version)"
```

#### Slick
```sbt
  // Play 2.9
  libraryDependencies += "io.github.felipebonezi" %% "play-actuator-slick-indicator" % "(version)"
  // Play 3.0
  libraryDependencies += "io.github.felipebonezi" %% "play-actuator-slick-indicator_play30" % "(version)"
```

### Redis Indicator

Show to you information about your Redis connection. 
**For now, it only works with [play-redis](https://github.com/KarelCemus/play-redis) as your connector.**

`play.actuator.health.indicators.redis = true`

```sbt
  // Play 2.9
  libraryDependencies += "io.github.felipebonezi" %% "play-actuator-redis-indicator" % "(version)"
  // Play 3.0
  libraryDependencies += "io.github.felipebonezi" %% "play-actuator-redis-indicator_play30" % "(version)"
```

## Info endpoint details

You can enable to get all operational system info 
inside the JSON return by `/actuator/info` route. Default is disabled.

`play.actuator.info.system.enabled = true`

## Cross-building

`play-actuator` is published for two Play majors. Pick the coordinate
suffix that matches the Play version your application is on:

| Your Play | Coordinate suffix     | Scala         |
|-----------|-----------------------|---------------|
| 2.9.x     | *(none, unsuffixed)*  | 2.13          |
| 3.0.x     | `_play30`             | 2.13 or 3.3   |

The Play 2.9 axis continues to publish under the original, unsuffixed
coordinates — existing consumers do **not** need to update their
`libraryDependencies` strings when they pick up a new release. Only the
new Play 3.0 axis introduces a coordinate suffix.

For local development, switch axes with the `play.version` JVM property
(or the equivalent `PLAY_VERSION` env var). The default is Play 2.9:

```bash
sbt -Dplay.version=2.9 ++2.13.18 compile test    # Play 2.9 / Scala 2.13
sbt -Dplay.version=3.0 ++2.13.18 compile test    # Play 3.0 / Scala 2.13
sbt -Dplay.version=3.0 ++3.3.6   compile test    # Play 3.0 / Scala 3
```

## Scala compatibility

This project is published for Scala `2.13` on both Play axes, plus Scala
`3.3` (LTS) on the Play 3.0 axis.

## Sponsors & Backers

If you find Play Actuator useful to you, please consider [become a backer](https://github.com/sponsors/felipebonezi).
If your company seems to feel the same, please consider [become a sponsor](https://github.com/sponsors/felipebonezi).

<div align="center">
  <a href="https://opencollective.com/felipebonezi" target="_blank">
    <img src="https://opencollective.com/felipebonezi/donate/button@2x.png?color=blue" width="250" />
  </a>
</div>

### Thank you to all our backers on OpenCollective!

<a href="https://opencollective.com/felipebonezi#section-contributors"><img src="https://opencollective.com/felipebonezi/organizations.svg?width=890&button=false&avatarHeight=46"></a>
<a href="https://opencollective.com/felipebonezi#section-contributors"><img src="https://opencollective.com/felipebonezi/individuals.svg?width=890&button=false&avatarHeight=46"></a>
