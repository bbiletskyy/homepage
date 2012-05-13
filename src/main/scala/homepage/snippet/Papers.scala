package homepage.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.RequestVar
import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import homepage.model.Message

class Papers {
  object qpx extends RequestVar(Full(""))

  def showPapers(xhtml: NodeSeq): NodeSeq = <p>Papers go here </p>
  def addPaper(xhtml: NodeSeq): NodeSeq = <p>Add paper </p>  
    
  /*{
    val msgSent = !(qpx.isEmpty || qpx.open_!.length == 0)
    if (msgSent) {
      val msg: Message = Message.create
      msg.text(qpx.open_!).save
    }
    val messages = Message.findAll
    val temp = messages.foldLeft("") { (str, msg) => str + " ### " + msg.text }
    bind("qp", xhtml,
      "update" --> textarea("", v => qpx(Full(v)))/* % ("cols" -> "10") % ("rows" -> "5")*/ % ("id" -> "update"),
      "submit" --> submit(?("Send Message"), () => {}),
      "messages" --> <div>{ temp }</div>)
  }*/
    
   
}
