package homepage.content

import homepage.model.ImpressingItem
import net.liftweb.db.StandardDBVendor
import net.liftweb.mapper.Schemifier
import net.liftweb.common.Logger
import scala.io.Source
import net.liftweb.mapper.DB
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.common.Full
import net.liftweb.util.Props
import net.liftweb.common.Empty


object ImpressingItemsData {
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
    Schemifier.destroyTables_!!(Schemifier.infoF(_), ImpressingItem)
    Schemifier.schemify(true, Schemifier.infoF(_), ImpressingItem)
  }

  private def impressingItemFromLine(s: String): ImpressingItem = {
    val originalWords = s.split("\t").map(w => w.replace("\"", "")).toList
    val words = originalWords ++ List.fill(5 - originalWords.size)("")
    assert(words.size == 5, "5 words needed to build an impressig item from string")
    ImpressingItem.create.title(words(0)).content(words(1)).youtubeIds(words(2)).urls(words(3)).tag(words(4))
  }

  /**Extracts impressing item objects from tab delimited text file */
  def impressingItemsFromTextFile(path: String): List[ImpressingItem] = Source.fromFile(path).getLines().drop(1).map(impressingItemFromLine).toList

  def main(args: Array[String]) {
    recreateTable
    impressingItemsFromTextFile("src/main/resources/impressing.txt").foreach(_.save)
    println("Added " + ImpressingItem.count + " impressing items")
  }
}