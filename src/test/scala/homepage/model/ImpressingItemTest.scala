package homepage.model
import org.specs.SpecificationWithJUnit

import org.specs._
import org.specs.matcher._
import net.liftweb.mapper._

class ImpressingItemTest extends SpecificationWithJUnit {

  "Impressing item" should {
    "added to the database " in {
      InMemoryDb.init
      val ii = ImpressingItem.create.title("Title1").content("Content1").youtubeIds("12345;1234567;").urls("[url|www.mail.ru]").save
      val items = ImpressingItem.findAll()
      items.size must_== 1
    }
  }
}