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

import scala.jdk.CollectionConverters._

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents

class MetricsController @Inject() (
    registry: MeterRegistry,
    prometheusRegistry: PrometheusMeterRegistry,
    cc: ControllerComponents
) extends BaseController {

  // GET /metrics — Spring-compatible: { "names": [...] }
  def list: Action[AnyContent] = Action {
    val names = registry.getMeters.asScala.iterator.map(_.getId.getName).toSet.toSeq.sorted
    Ok(Json.obj("names" -> names))
  }

  // GET /metrics/:name — Spring-compatible meter detail JSON.
  def detail(name: String): Action[AnyContent] = Action {
    val meters = registry.getMeters.asScala.filter(_.getId.getName == name).toSeq
    if (meters.isEmpty) {
      NotFound(Json.obj("error" -> s"meter not found: $name"))
    } else {
      Ok(meterToJson(name, meters))
    }
  }

  // GET /metrics/prometheus — text/plain Prometheus exposition.
  def prometheus: Action[AnyContent] = Action {
    val body = prometheusRegistry.scrape()
    Ok(body).as("text/plain; version=0.0.4; charset=utf-8")
  }

  protected override def controllerComponents: ControllerComponents = this.cc

  private def meterToJson(name: String, meters: Seq[Meter]): JsObject = {
    val measurements = meters.flatMap { m =>
      m.measure().asScala.map { ms =>
        Json.obj(
          "statistic" -> ms.getStatistic.toString,
          "value"     -> ms.getValue
        )
      }
    }
    val tags = meters
      .flatMap(_.getId.getTags.asScala)
      .groupBy(_.getKey)
      .map { case (k, vs) => Json.obj("tag" -> k, "values" -> vs.map(_.getValue).distinct) }
      .toSeq
    val baseUnit = Option(meters.head.getId.getBaseUnit).map(JsString.apply)
    val base = Json.obj(
      "name"            -> name,
      "measurements"    -> measurements,
      "availableTags"   -> tags
    )
    baseUnit.fold(base)(u => base + ("baseUnit" -> u))
  }

}
