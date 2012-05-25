package homepage.model
import org.specs.SpecificationWithJUnit
import homepage.content.PapersData
import homepage.content.TalksData

class TalksTest extends SpecificationWithJUnit {
  
  "Talks initial data" should {
    "be successfully added to the database" in {
      TalksData.recreateTable
      TalksData.talksFromFile("src/test/resources/talks.txt").foreach(_.save)
      Talk.count must_== 8
    }
  }
}