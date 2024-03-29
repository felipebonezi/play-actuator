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

import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents
import play.actuator.ActuatorEnum.Down
import play.actuator.ActuatorEnum.Status
import play.actuator.ActuatorEnum.Up
import play.actuator.health.HealthService
import play.actuator.info.InfoService
import play.api.libs.json.Json.toJson

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ActuatorController @Inject() (healthService: HealthService, infoService: InfoService, cc: ControllerComponents)(
    implicit ec: ExecutionContext
) extends BaseController {

  def health: Action[AnyContent] = Action {
    val indicators = this.healthService.getIndicators
    if (indicators.nonEmpty) {
      val status = if (indicators.exists(indicator => indicator.status == Down)) {
        Down
      } else {
        Up
      }
      Ok(Json.obj("status" -> status, "indicators" -> toJson(indicators)))
    } else {
      Ok(Json.obj("status" -> this.healthService.globalStatus))
    }
  }

  def info: Action[AnyContent] = Action {
    Ok(this.infoService.getBuildInfos)
  }

  protected override def controllerComponents: ControllerComponents = this.cc

}
