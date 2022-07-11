# play-actuator

[![CI](https://github.com/felipebonezi/play-actuator/actions/workflows/continouos-integration.yml/badge.svg)](https://github.com/felipebonezi/play-actuator/actions/workflows/continouos-integration.yml)
[![Renovate](https://img.shields.io/badge/renovate-enabled-brightgreen.svg)](https://renovatebot.com)
[![Version](https://img.shields.io/github/v/release/felipebonezi/play-actuator?logo=java)](https://github.com/felipebonezi/play-actuator/releases)
[![CLA assistant](https://cla-assistant.io/readme/badge/felipebonezi/play-actuator)](https://cla-assistant.io/felipebonezi/play-actuator)
[![Licence](https://img.shields.io/github/license/felipebonezi/play-actuator?color=blue)](https://github.com/felipebonezi/play-actuator/blob/main/LICENSE)

Play! Framework plugin with actuator actions about your application.

This project is inspired by `Spring Boot Actuator` architecture
([Check this post to know more about it](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator))
.

## How to use?

You must import as project dependency in your `build.sbt` file.

```sbt
  libraryDependencies ++= "io.github.felipebonezi" % "play-actuator" % "(version)"
```

After that, you need to configure `play.actuator.ActuatorRouter` into project `conf/routes` file.

```
->      /actuator      play.actuator.ActuatorRouter
```

Run your project and check Actuators endpoints - e.g. `/actuator/health`.

## Actuator endpoints

| Endpoint ID | Description                                  | Path                | Ready to use |
|-------------|----------------------------------------------|---------------------|--------------|
| health      | Displays your application’s health status.   | `/actuator/health`  | ✔️           |
| info        | Displays information about your application. | `/actuator/info`    | ✖️           |
| logfile     | Returns the contents of the log file.        | `/actuator/logfile` | ✖️           |

## Health endpoint details

You can check all health indicators by simple activating each configuration.

### Disk Space Indicator

`play.actuator.health.indicators.diskSpace = true`
