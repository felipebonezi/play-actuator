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
package play.actuator.health

import play.actuator.ActuatorEnum.Status
import play.actuator.ActuatorEnum.Up
import play.actuator.ActuatorEnum.Down
import play.actuator.health.indicator.DiskSpaceIndicator
import play.api.Configuration

import javax.inject.Singleton
import javax.inject.Named
import javax.inject.Inject
import scala.collection.mutable

@Singleton
class HealthService @Inject() (
    config: Configuration,
    diskSpaceIndicator: DiskSpaceIndicator
//    @Named("jdbcIndicator") jdbcIndicator: DatabaseIndicator,
//    @Named("slickIndicator") slickIndicator: DatabaseIndicator,
//    redisIndicator: RedisIndicator
) {

  def globalStatus: Status =
    if (getIndicators.exists(h => h.status == Down)) {
      Down
    } else {
      Up
    }

  def getIndicators: Seq[Health] = {
    val indicators = mutable.Buffer[Health]()
    if (isIndicatorActive("diskSpace")) {
      val builder = new HealthBuilder("diskSpace")
      this.diskSpaceIndicator.info(builder)
      indicators.append(builder.build)
    }

//    if (isIndicatorActive("jdbc")) {
//      val builder = new HealthBuilder("jdbc")
//      this.jdbcIndicator.info(builder)
//      indicators.append(builder.build)
//    }
//
//    if (isIndicatorActive("slick")) {
//      val builder = new HealthBuilder("slick")
//      this.slickIndicator.info(builder)
//      indicators.append(builder.build)
//    }
//
//    if (isIndicatorActive("redis")) {
//      val builder = new HealthBuilder("redis")
//      this.redisIndicator.info(builder)
//      indicators.append(builder.build)
//    }

    indicators.toSeq
  }

  private def isIndicatorActive(name: String): Boolean =
    this.config
      .getOptional[Boolean](s"play.actuator.health.indicators.$name")
      .orElse(Some(false))
      .get

}
