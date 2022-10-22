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
import play.api.libs.json.JsNull
import play.api.libs.json.JsNumber
import play.api.libs.json.JsString
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Health(name: String, status: Status, details: Map[String, Any]) {
  override def toString: String = s"Health(status=$status, details=$details)"
}

object Health {
  implicit val writes: Writes[Health] =
    (o: Health) =>
      Json.obj(
        o.name -> Json.obj(
          "status" -> o.status,
          "details" -> Json.toJson(o.details.map { case (key, value) =>
            key -> (value match {
              case x: String => JsString(x)
              case x: Int    => JsNumber(x)
              case x: Long   => JsNumber(x)
              case _         => JsNull
            })
          })
        )
      )
}
