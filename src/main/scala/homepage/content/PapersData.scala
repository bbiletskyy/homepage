package homepage.content
import net.liftweb.db.StandardDBVendor
import net.liftweb.common.Empty
import net.liftweb.util.Props
import net.liftweb.common.Logger
import net.liftweb.common.Full
import net.liftweb.mapper.Schemifier
import homepage.model.Paper
import net.liftweb.mapper.DB
import net.liftweb.db.DefaultConnectionIdentifier
import scala.io.Source
import scala.io.UTF8Codec

object PapersData {
  def vendorInMemory = new StandardDBVendor("org.h2.Driver", "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE", Empty, Empty)
  def vendorDefault = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
    Props.get("db.url") openOr
      "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
    Props.get("db.user"), Props.get("db.password"))

  val vendor = vendorDefault
  Logger.setup = Full(net.liftweb.util.LoggingAutoConfigurer())
  Logger.setup.foreach { _.apply }

  def recreateTable() {
    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    Schemifier.destroyTables_!!(Schemifier.infoF(_), Paper)
    Schemifier.schemify(true, Schemifier.infoF(_), Paper)
  }

  def paperFromLine(s: String): Paper = {
    val originalWords = s.split("\t").map(w => w.replace("\"", "")).toList
    val words = originalWords ++ List.fill(5 - originalWords.size)("")
    assert(words.size == 5, "5 words needed to build an impressig item from string")
    Paper.create.authors(words(0)).title(words(1)).journal(words(2)).year(words(3).toInt).url(words(4))
  }

  //ISO-8859-1, UTF-8
  def papersFromFile(path: String): List[Paper] = Source.fromFile(path).getLines().drop(1).map(paperFromLine).toList

  def main(args: Array[String]) {
    recreateTable
    papersFromFile("D:/Workspaces/Scala/Lift/homepage/doc/papers.txt").foreach(_.save)
    println("Papers created: " + Paper.count)
  }
}