package homepage.model
import net.liftweb.mapper._

object Talk extends Talk with KeyedMetaMapper[Long, Talk] {
  override def dbTableName = "talk"
  override def fieldOrder = id :: authors :: title :: conference :: url :: year :: tag :: Nil
}

class Talk extends KeyedMapper[Long, Talk] {
  def getSingleton = Talk
  def primaryKeyField = id

  object id extends MappedLongIndex(this)
  object authors extends MappedString(this, 1024)
  object title extends MappedString(this, 1024)
  object conference extends MappedString(this, 1024)
  object url extends MappedString(this, 1024)
  object year extends MappedString(this, 4)
  object tag extends MappedString(this, 64)
}