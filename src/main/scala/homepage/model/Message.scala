package homepage.model
import net.liftweb.mapper._

object Message extends Message with KeyedMetaMapper[Long, Message] {
  override def dbTableName = "message"
  override def fieldOrder = id :: text :: Nil
}

class Message extends KeyedMapper[Long, Message] {
  def getSingleton = Message // what's the "meta" object
  def primaryKeyField = id

  object id extends MappedLongIndex(this)
  object text extends MappedString(this, 1400)
}