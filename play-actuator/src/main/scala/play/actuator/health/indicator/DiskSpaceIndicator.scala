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

import play.actuator.ActuatorEnum.Down
import play.actuator.ActuatorEnum.Status
import play.actuator.ActuatorEnum.Up
import play.actuator.health.HealthBuilder

import java.io.File
import javax.inject.Singleton

@Singleton
class DiskSpaceIndicator extends BaseHealthIndicator {

  private val folder = new File("/")

  private val status: Status = {
    val usableSpace = folder.getUsableSpace
    if (usableSpace > 0) {
      Up
    } else {
      Down
    }
  }

  override def info(builder: HealthBuilder): Unit =
    builder
      .withStatus(this.status)
      .withDetail("total", folder.getTotalSpace)
      .withDetail("free", folder.getFreeSpace)
      .withDetail("usable", folder.getUsableSpace)

}
