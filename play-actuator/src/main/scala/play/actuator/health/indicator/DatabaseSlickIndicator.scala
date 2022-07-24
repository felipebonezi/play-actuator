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

import com.typesafe.config.Config
import play.actuator.ActuatorEnum.Down
import play.actuator.ActuatorEnum.Up
import play.actuator.health.HealthBuilder
import play.actuator.health.indicator.DatabaseSlickIndicator.DB_TIMEOUT_SECS
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Connection
import javax.inject.Inject

class DatabaseSlickIndicator @Inject() (
    val config: Config,
    val dbConfigProvider: DatabaseConfigProvider
) extends DatabaseIndicator
    with HasDatabaseConfigProvider[JdbcProfile] {

  private var connectionRef: Option[Connection] = None

  private[this] def openConnection(): Connection = {
    val connection = this.db.source.createConnection()
    this.connectionRef = Some(connection)
    connection
  }

  private[this] def getConnection: Connection = {
    if (this.connectionRef.isEmpty) {
      openConnection()
    } else {
      val connection = this.connectionRef.get
      if (connection.isClosed) {
        this.connectionRef = None
        openConnection()
      } else {
        connection.isValid(DB_TIMEOUT_SECS)
        connection
      }
    }
  }

  override def info(builder: HealthBuilder): Unit = {
    try {
      val metaData = this.getConnection.getMetaData
      builder
        .withStatus(Up)
        .withDetail("name", this.dbConfigProvider.get.profileName)
        .withDetail("url", this.config.getString("slick.dbs.default.db.url"))
        .withDetail("driver", metaData.getDriverName)
    } catch {
      case e: Exception =>
        builder
          .withStatus(Down)
          .withDetail("name", this.dbConfigProvider.get.profileName)
          .withDetail("url", this.config.getString("slick.dbs.default.db.url"))
          .withDetail("exception", e.getMessage)
    }
  }

}

object DatabaseSlickIndicator {
  private val DB_TIMEOUT_SECS = 5000
}
