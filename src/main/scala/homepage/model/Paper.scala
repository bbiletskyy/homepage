package homepage.model
import net.liftweb.mapper._

object Paper extends Paper with KeyedMetaMapper[Long, Paper] {
  override def dbTableName = "paper"
  override def fieldOrder = id :: authors :: title :: url :: Nil
}

class Paper extends KeyedMapper[Long, Paper] {
  def getSingleton = Paper
  def primaryKeyField = id

  object id extends MappedLongIndex(this)
  object authors extends MappedString(this, 1024)
  object title extends MappedString(this, 1024)
  object journal extends MappedString(this, 1024)
  object url extends MappedString(this, 1024)
  object year extends MappedInt(this)
  //object year extends MappedString(this, 4)
  //object tag extends MappedString(this, 64)
  
}