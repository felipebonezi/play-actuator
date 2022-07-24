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
package play.actuator.health.indicator
import com.typesafe.config.Config
import play.actuator.ActuatorEnum.Down
import play.actuator.ActuatorEnum.Up
import play.actuator.health.HealthBuilder
import play.api.cache.redis.RedisConnector

import javax.inject.Inject
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class PlayRedisIndicator @Inject() (
    config: Config,
    connector: RedisConnector
) extends RedisIndicator {

  override def info(builder: HealthBuilder): Unit = {
    if (this.config.getString("play.cache.redis.recovery") != "log-and-fail") {
      throw new IllegalArgumentException("You need to use 'log-and-fail' recovery for Redis.")
    }

    try {
      Await.result(this.connector.ping(), 3.seconds)
      builder
        .withStatus(Up)
        .withDetail("source", this.config.getString("play.cache.redis.source"))
    } catch {
      case e: Exception =>
        builder
          .withStatus(Down)
          .withDetail("message", "Redis connection failed!")
          .withDetail("exception", e.getMessage)
    }
  }

}
