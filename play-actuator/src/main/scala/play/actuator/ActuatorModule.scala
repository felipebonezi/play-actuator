/*
 * Copyright (c) 2022 Felipe Bonezi <https://about.me/felipebonezi>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package play.actuator
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import play.actuator.ActuatorEnum.Up
import play.actuator.health.HealthBuilder
import play.actuator.health.indicator.DatabaseIndicator
import play.actuator.health.indicator.DatabaseJdbcIndicator
import play.actuator.health.indicator.DatabaseSlickIndicator
import play.actuator.health.indicator.PlayRedisIndicator
import play.actuator.health.indicator.RedisIndicator
import play.api.Configuration
import play.api.Environment

class ActuatorModule(environment: Environment, config: Configuration) extends AbstractModule {

  override def configure(): Unit = {
    if (config.has("db")) {
      bind(classOf[DatabaseIndicator])
        .annotatedWith(Names.named("jdbcIndicator"))
        .to(classOf[DatabaseJdbcIndicator])
    } else {
      bind(classOf[DatabaseIndicator])
        .annotatedWith(Names.named("jdbcIndicator"))
        .toInstance((builder: HealthBuilder) => {
          builder
            .withStatus(Up)
            .withDetail("message", "Application without database connection.")
        })
    }
    if (config.has("slick")) {
      bind(classOf[DatabaseIndicator])
        .annotatedWith(Names.named("slickIndicator"))
        .to(classOf[DatabaseSlickIndicator])
    } else {
      bind(classOf[DatabaseIndicator])
        .annotatedWith(Names.named("slickIndicator"))
        .toInstance((builder: HealthBuilder) => {
          builder
            .withStatus(Up)
            .withDetail("message", "Application without Slick connection.")
        })
    }
    if (config.has("play.cache.redis")) {
      bind(classOf[RedisIndicator]).to(classOf[PlayRedisIndicator])
    } else {
      bind(classOf[RedisIndicator]).toInstance((builder: HealthBuilder) => {
        builder
          .withStatus(Up)
          .withDetail("message", "Application without Redis connection.")
      })
    }
  }

}
