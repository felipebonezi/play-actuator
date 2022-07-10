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
package controllers
import play.api.mvc.Result
import play.api.test._

import scala.concurrent.Future

class ActuatorControllerSpec extends PlaySpecification {

  "The ActuatorController" should {
    "return if service is healthy from route \"/actuator/health\"" in new WithApplication(builder =>
      builder.configure(inMemoryDatabase("test"))
    ) with Injecting {
      val result: Future[Result] = route(app, FakeRequest(GET, "/actuator/health")).get

      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      contentAsString(result) must equalTo("{\"status\":\"UP\"}")
    }
  }

}
