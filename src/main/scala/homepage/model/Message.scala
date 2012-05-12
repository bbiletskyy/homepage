package homepage.model
import net.liftweb.mapper._

object Message extends Message with KeyedMetaMapper[Long, Message] {
  override def dbTableName = "message"
  override def fieldOrder = id :: text :: Nil
}

class Message extends KeyedMapper[Long, Message] {
  def getSingleton = Message 
  def primaryKeyField = id

  object id extends MappedLongIndex(this)
  object text extends MappedText(this)
  //  object text extends MappedTextarea(this, 2048) {
  //    override def textareaRows = 5
  //    override def textareaCols = 25
  //    override def displayName = "Message body"
  //  }
}