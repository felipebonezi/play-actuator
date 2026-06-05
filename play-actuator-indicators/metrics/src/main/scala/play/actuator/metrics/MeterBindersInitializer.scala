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
import javax.inject.Singleton

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import play.api.Configuration

// Eagerly registers the configured JVM/system MeterBinders against the
// MeterRegistry at app boot. Each binder is gated on a config flag so
// applications can drop expensive groups (e.g. JvmGcMetrics on hot paths).
@Singleton
class MeterBindersInitializer @Inject() (registry: MeterRegistry, config: Configuration) {

  private def maybe(key: String, mk: => MeterBinder): Option[MeterBinder] =
    if (enabled(key)) Some(mk) else None

  private def enabled(key: String): Boolean =
    config.getOptional[Boolean](s"play.actuator.metrics.bindings.$key").getOrElse(true)

  private val binders: Seq[MeterBinder] = Seq(
    maybe("jvm-memory", new JvmMemoryMetrics),
    maybe("jvm-gc", new JvmGcMetrics),
    maybe("jvm-threads", new JvmThreadMetrics),
    maybe("classloader", new ClassLoaderMetrics),
    maybe("processor", new ProcessorMetrics),
    maybe("uptime", new UptimeMetrics),
    maybe("file-descriptors", new FileDescriptorMetrics)
  ).flatten

  binders.foreach(_.bindTo(registry))
}
