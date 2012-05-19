package homepage.model
import scala.io.Source
import net.liftweb.util.Props

class InitialData {

}

object InitialData {
  
  private def str2ii(s: String): ImpressingItem = {
    val originalWords = s.split("\t").map(w => w.replace("\"", "")).toList
    val words = originalWords ++ List.fill(5 - originalWords.size)("")
    assert(words.size == 5, "5 words needed")
    ImpressingItem.create.title(words(0)).content(words(1)).youtubeIds(words(2)).urls(words(3)).tag(words(4))
  }

  def impressingItemsFromTextFile(path: String): List[ImpressingItem] = Source.fromFile(path).getLines().drop(1).map(str2ii).toList
    
  def main(args: Array[String]) {
    DefaultDb.init
    impressingItemsFromTextFile("D:/Workspaces/Scala/Lift/homepage/doc/impressing.txt").foreach(_.save)
    println("Added " + ImpressingItem.count + " impressing items")
  }

}