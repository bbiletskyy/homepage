package homepage.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.RequestVar
import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import homepage.model.Message



class PostMessage {
  object qpx extends RequestVar(Full("")) // default is empty string
  
  def show(xhtml: NodeSeq): NodeSeq = {
    val msgSent = !(qpx.isEmpty || qpx.open_!.length == 0)
    if (msgSent){
        val msg:Message = Message.create
        msg.text(qpx.open_!).save
    }
    val messages = Message.findAll
    val temp = messages.foldLeft(""){(str, msg) => str + " ### " + msg.text}
    bind("qp", xhtml,
        "update" --> text("", v => qpx(Full(v))) % ("size" -> "10") % ("id" -> "update"),
        "submit" --> submit(?("Update"), ()=>{}),
        "messages" --> <div>{temp}</div>
    )
  }
}
