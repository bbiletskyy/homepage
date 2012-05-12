package homepage.model

import net.liftweb.mapper._

object ImpressingItem extends ImpressingItem with KeyedMetaMapper[Long, ImpressingItem] {
  override def dbTableName = "impressing_item"
  override def fieldOrder = id :: title :: content :: Nil
}

class ImpressingItem extends KeyedMapper[Long, ImpressingItem] {
  def getSingleton = ImpressingItem
  def primaryKeyField = id

  object id extends MappedLongIndex(this)
  object title extends MappedString(this, 1024)
  object content extends MappedText(this)
  //  object content extends MappedTextarea(this, 2048) {
  //    override def textareaRows  = 10
  //    override def textareaCols = 50
  //    override def displayName = "Impressing item's content"
  //  }
  //  object youtubeIds extends MappedText(this)

}