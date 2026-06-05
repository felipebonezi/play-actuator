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

import javax.inject.Inject

import scala.concurrent.ExecutionContext

import play.actuator.ActuatorEnum.Down
import play.actuator.health.HealthService
import play.actuator.info.InfoService
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJson
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents

class ActuatorController @Inject() (
    healthService: HealthService,
    infoService: InfoService,
    cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BaseController {

  def health: Action[AnyContent] = Action {
    respondHealth(group = None)
  }

  // GET /health/:group — Spring-compatible `liveness`/`readiness` (or any user-defined group).
  // Unknown group → 404 so k8s probes fail loudly on misconfiguration.
  def healthGroup(group: String): Action[AnyContent] = Action {
    if (this.healthService.knowsGroup(group)) {
      respondHealth(group = Some(group))
    } else {
      NotFound(Json.obj("error" -> s"unknown health group: $group"))
    }
  }

  def info: Action[AnyContent] = Action {
    Ok(this.infoService.getBuildInfos)
  }

  protected override def controllerComponents: ControllerComponents = this.cc

  private def respondHealth(group: Option[String]) = {
    val indicators = this.healthService.getIndicators(group)
    val status     = this.healthService.globalStatus(group)
    val body: JsObject =
      if (indicators.nonEmpty) {
        Json.obj("status" -> status, "indicators" -> toJson(indicators))
      } else {
        Json.obj("status" -> status)
      }
    // 503 on aggregated Down lets k8s probes treat the response as a hard failure.
    if (status == Down) {
      ServiceUnavailable(body)
    } else {
      Ok(body)
    }
  }

}
