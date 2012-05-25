package homepage.model
import org.specs.SpecificationWithJUnit
import homepage.content.ImpressingItemsData

class ImpressingItemTest extends SpecificationWithJUnit {
  
  "Impressing items initial data" should {
    "be successfully added to the database" in {
      ImpressingItemsData.recreateTable
      ImpressingItemsData.impressingItemsFromTextFile("src/test/resources/impressing.txt").foreach(_.save)
      ImpressingItem.count must_== 37
    }
  }
}