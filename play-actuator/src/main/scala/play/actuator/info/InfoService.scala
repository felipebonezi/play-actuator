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
package play.actuator.info

import play.actuator.build.BuildInfo
import play.api.Configuration
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Json

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InfoService @Inject() (config: Configuration) {

  import scala.collection.JavaConverters._

  def getBuildInfos: JsValue = {
    var buildInfos = Json.parse(BuildInfo.toJson).as[JsObject]
    buildInfos = buildInfos + ("dateTime" -> JsString(LocalDateTime.now().format(ISO_DATE_TIME)))

    if (isSystemInfoActive) {
      import scala.collection.immutable.ListMap

      val systemInfos       = System.getProperties.asScala.map(p => (p._1, JsString(p._2)))
      val sortedSystemInfos = ListMap(systemInfos.toSeq.sortBy(_._1): _*)
      buildInfos = buildInfos + ("system" -> JsObject(sortedSystemInfos))
    }

    buildInfos
  }

  private def isSystemInfoActive: Boolean =
    this.config
      .getOptional[Boolean](s"play.actuator.info.system.enabled")
      .orElse(Some(false))
      .get

}
