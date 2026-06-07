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
package play.actuator.metrics

import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

import scala.jdk.CollectionConverters._

import io.micrometer.core.instrument.Tag
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import play.api.Configuration

@Singleton
class MeterRegistryProvider @Inject() (config: Configuration) extends Provider[PrometheusMeterRegistry] {
  override def get(): PrometheusMeterRegistry = {
    val registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    val tags     = commonTags(config)
    if (tags.nonEmpty) {
      registry.config().commonTags(tags.asJava)
    }
    registry
  }

  private def commonTags(c: Configuration): Seq[Tag] = {
    val key = "play.actuator.metrics.common-tags"
    if (!c.has(key)) {
      Seq.empty
    } else {
      c.underlying
        .getObject(key)
        .entrySet()
        .asScala
        .iterator
        .map { e =>
          Tag.of(e.getKey, e.getValue.unwrapped().toString)
        }
        .toSeq
    }
  }
}
