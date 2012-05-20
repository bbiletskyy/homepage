package homepage.content
import net.liftweb.db.StandardDBVendor
import net.liftweb.mapper.Schemifier
import net.liftweb.common.Logger
import net.liftweb.mapper.DB
import homepage.model.Talk
import net.liftweb.util.Props
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.common.Full
import net.liftweb.common.Empty
import homepage.model.Paper
import scala.io.Source

object TalksData {
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
    Schemifier.destroyTables_!!(Schemifier.infoF(_), Talk)
    Schemifier.schemify(true, Schemifier.infoF(_), Talk)
  }

  def talkFromLine(s: String): Talk = {
    val originalWords = s.split("\t").map(w => w.replace("\"", "")).toList
    val words = originalWords ++ List.fill(5 - originalWords.size)("")
    assert(words.size == 5, "5 words needed to build an impressig item from string")
    Talk.create.authors(words(0)).title(words(1)).conference(words(2)).year(words(3).toInt).url(words(4))
  }

  //ISO-8859-1, UTF-8
  def talksFromFile(path: String): List[Talk] = Source.fromFile(path).getLines().drop(1).map(talkFromLine).toList

  def main(args: Array[String]) {
    recreateTable
    talksFromFile("D:/Workspaces/Scala/Lift/homepage/doc/talks.txt").foreach(_.save)
    println("Talks created: " + Paper.count)
  }
}