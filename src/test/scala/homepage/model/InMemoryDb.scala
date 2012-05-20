package homepage.model
import net.liftweb.common._
import net.liftweb.common.Empty
import net.liftweb.db.StandardDBVendor
import net.liftweb.mapper._
import net.liftweb.util._

object InMemoryDb {
  val vendor =
    new StandardDBVendor("org.h2.Driver","jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
      Empty, Empty)
  Logger.setup = Full(net.liftweb.util.LoggingAutoConfigurer())
  Logger.setup.foreach { _.apply }

  def init {
    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    Schemifier.destroyTables_!!(Schemifier.infoF(_), ImpressingItem)
    Schemifier.schemify(true, Schemifier.infoF(_), ImpressingItem)
  }

  
  
  
}