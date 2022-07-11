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

import play.actuator.ActuatorEnum.Down
import play.actuator.ActuatorEnum.Status
import play.actuator.ActuatorEnum.Up
import play.actuator.health.indicator.DiskSpaceIndicatorBase
import play.api.Configuration

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthService @Inject() ()(config: Configuration) {

  def globalStatus: Status =
    if (getIndicators.exists(h => h.status == Down)) {
      Down
    } else {
      Up
    }

  def getIndicators: Seq[Health] = {
    if (isIndicatorActive("diskSpace")) {
      val builder            = new HealthBuilder()
      val diskSpaceIndicator = new DiskSpaceIndicatorBase()
      diskSpaceIndicator.info(builder)
      Seq(builder.build)
    } else {
      Seq.empty[Health]
    }
  }

  private def isIndicatorActive(name: String): Boolean =
    this.config
      .getOptional[Boolean](s"play.actuator.health.indicators.$name")
      .orElse(Some(false))
      .get

}
