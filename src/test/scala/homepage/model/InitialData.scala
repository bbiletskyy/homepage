package homepage.model
import scala.io.Source

class InitialData{}

object InitialData {
  private def impressingItemFromString(s: String): ImpressingItem = {
    val originalWords = s.split("\t").map(w => w.replace("\"", "")).toList
    val words = originalWords ++ List.fill(5 - originalWords.size)("")
    assert(words.size == 5, "5 words needed to build an impressig item from string")
    ImpressingItem.create.title(words(0)).content(words(1)).youtubeIds(words(2)).urls(words(3)).tag(words(4))
  }
  
  private def paperFromString(s: String): Paper = {
    val originalWords = s.split("\t").map(w => w.replace("\"", "")).toList
    val words = originalWords ++ List.fill(5 - originalWords.size)("")
    assert(words.size == 5, "5 words needed to build a paper  from string")
    //ImpressingItem.create.title(words(0)).content(words(1)).youtubeIds(words(2)).urls(words(3)).tag(words(4))
    Paper.create.title(words(0))
  }
  
  /**Extracts impressing item objects from tab delimited text file */
  def impressingItemsFromTextFile(path: String): List[ImpressingItem] = Source.fromFile(path).getLines().drop(1).map(impressingItemFromString).toList
    
  def papersFromTextFile(path: String): List[Paper] = Source.fromFile(path).getLines().drop(1).map(paperFromString).toList
  
  def main(args: Array[String]) {
    InMemoryDb.init
    //DefaultDb.init
    //val papers = papersFromTextFile("D:/Workspaces/Scala/Lift/homepage/doc/papers.txt")
    //println("Papers extracted")
    
    impressingItemsFromTextFile("D:/Workspaces/Scala/Lift/homepage/doc/impressing.txt").foreach(_.save)
    println("Added " + ImpressingItem.count + " impressing items")
  }

}