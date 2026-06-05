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

import javax.inject.Inject
import javax.inject.Singleton

import scala.jdk.CollectionConverters._

import com.typesafe.config.ConfigObject
import play.api.Configuration

@Singleton
class HealthGroupsConfig @Inject() (config: Configuration) {

  // Parse `play.actuator.health.groups.<name>.{include,exclude}` into a Map.
  // Defaults (liveness/readiness) ship via reference.conf so consumers get
  // Kubernetes-ready probe endpoints with zero application.conf changes.
  val groups: Map[String, HealthGroup] = {
    val key = "play.actuator.health.groups"
    if (!config.has(key)) Map.empty
    else {
      val obj = config.underlying.getObject(key)
      obj.entrySet().asScala.iterator.map { e =>
        val name = e.getKey
        val gc   = e.getValue.asInstanceOf[ConfigObject].toConfig
        val include =
          if (gc.hasPath("include")) gc.getStringList("include").asScala.toSet else Set.empty[String]
        val exclude =
          if (gc.hasPath("exclude")) gc.getStringList("exclude").asScala.toSet else Set.empty[String]
        name -> HealthGroup(name, include, exclude)
      }.toMap
    }
  }

  def get(name: String): Option[HealthGroup] = groups.get(name)
}
