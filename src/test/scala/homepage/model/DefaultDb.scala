package homepage.model
import net.liftweb.db.StandardDBVendor
import net.liftweb.common.Empty

import net.liftweb.mapper._
import net.liftweb.common._
import net.liftweb.util._
import java.sql._

/** If no default db is defined, then in-memory db is used. */
object DefaultDb {
  val vendor =
    new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
      Props.get("db.url") openOr
        "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
      Props.get("db.user"), Props.get("db.password"))
  Logger.setup = Full(net.liftweb.util.LoggingAutoConfigurer())
  Logger.setup.foreach { _.apply }

  def init {
    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    Schemifier.destroyTables_!!(Schemifier.infoF(_), ImpressingItem)
    Schemifier.schemify(true, Schemifier.infoF(_), ImpressingItem)
  }

}