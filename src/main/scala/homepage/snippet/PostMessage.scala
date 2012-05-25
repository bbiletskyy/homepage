package homepage.snippet

import scala.xml.NodeSeq
import homepage.model.Message
import net.liftweb.common.Full
import net.liftweb.http.SHtml._
import net.liftweb.http.S._
import net.liftweb.http.RequestVar
import net.liftweb.util.Helpers._
import net.liftweb.util.Mailer
import net.liftweb.util.Mailer.{ From, Subject, To }
import net.liftweb.http.S

class PostMessage {
  object messageReqVar extends RequestVar(Full(""))

  def show(xhtml: NodeSeq): NodeSeq = {
    val msgSent = !(messageReqVar.isEmpty || messageReqVar.open_!.length == 0)
    if (msgSent) {
      val msg: Message = Message.create
      val text = messageReqVar.open_!
      msg.text(text).save
      sendMessage(text)
      S.notice("postMessageForm", "Thank you for your message")
    }
    val messages = Message.findAll

    bind("qp", xhtml,
      "update" --> textarea("", v => messageReqVar(Full(v))) % ("id" -> "update"),
      "submit" --> submit(?("Send Message"), () => {}))
  }

  def sendMessage(messageBody: String) {
    println("Sending mail...")
    val html = <html>
                 <head>
                   <title>A message from particlez.net visitor</title>
                 </head>
                 <body>
                   { messageBody }
                 </body>
               </html>

    Mailer.sendMail(
      From("Myself <admin@particlez.net>"),
      Subject("A message from particlez.net visitor"),
      To("borys.biletskyy@gmail.com"),
      html)
  }

}
