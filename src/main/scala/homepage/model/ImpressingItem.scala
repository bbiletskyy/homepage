package homepage.model

import net.liftweb.mapper._

object ImpressingItem extends ImpressingItem with KeyedMetaMapper[Long, ImpressingItem] {
  override def dbTableName = "impressing_item"
  override def fieldOrder = id :: title :: content :: youtubeIds :: urls :: tag :: Nil
}

class ImpressingItem extends KeyedMapper[Long, ImpressingItem] {
  def getSingleton = ImpressingItem
  def primaryKeyField = id

  object id extends MappedLongIndex(this)
  object title extends MappedString(this, 1024)
  object content extends MappedText(this)
  object youtubeIds extends MappedString(this, 64)
  object urls extends MappedString(this, 64)
  object tag extends MappedString(this, 64)

}