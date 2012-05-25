package homepage.model
import org.specs.SpecificationWithJUnit

import homepage.content.PapersData

class PapersTest extends SpecificationWithJUnit {
  
  "Papers initial data" should {
    "be successfully added to the database" in {
      PapersData.recreateTable
      PapersData.papersFromFile("src/test/resources/papers.txt").foreach(_.save)
      Paper.count must_== 14
    }
  }
}