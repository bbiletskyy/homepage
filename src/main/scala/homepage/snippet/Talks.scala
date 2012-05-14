package homepage.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.RequestVar
import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import homepage.model.Paper
import homepage.model.Talk

class Talks {

  def showTalks(xhtml: NodeSeq): NodeSeq = {
    val talks = Talk.findAll.map(p => p.title).mkString("; ")
    <div>{ talks }</div>
  }

  def addTalk(xhtml: NodeSeq): NodeSeq = {
    object authorsReqVar extends RequestVar(Full(""))
    object titleReqVar extends RequestVar(Full(""))
    object conferenceReqVar extends RequestVar(Full(""))
    object tagReqVar extends RequestVar(Full(""))
    object urlReqVar extends RequestVar(Full(""))
    object yearReqVar extends RequestVar(Full(""))

    def isEmpty(r: RequestVar[Full[String]]): Boolean = r.isEmpty || r.open_!.length == 0
    def fieldsValid = !(isEmpty(authorsReqVar) || isEmpty(titleReqVar) || isEmpty(conferenceReqVar) || isEmpty(tagReqVar) || isEmpty(urlReqVar) || isEmpty(yearReqVar))

    if (fieldsValid) {
      val talk: Talk = Talk.create
      talk.authors(authorsReqVar.open_!).title(titleReqVar.open_!).conference(conferenceReqVar.open_!).tag(tagReqVar.open_!).url(urlReqVar.open_!).year(yearReqVar.open_!).save
    }

    bind("ap", xhtml,
      "authors" --> text("", v => authorsReqVar(Full(v))) % ("size" -> "50") % ("id" -> "authors"),
      "title" --> text("", v => titleReqVar(Full(v))) % ("size" -> "50") % ("id" -> "title"),
      "conference" --> text("", v => conferenceReqVar(Full(v))) % ("size" -> "50") % ("id" -> "conference"),
      "tag" --> text("", v => tagReqVar(Full(v))) % ("size" -> "50") % ("id" -> "tag"),
      "url" --> text("", v => urlReqVar(Full(v))) % ("size" -> "50") % ("id" -> "url"),
      "year" --> text("", v => yearReqVar(Full(v))) % ("size" -> "4") % ("id" -> "year"),
      "submit" --> submit(?("Add Talk"), () => {}),
      "messages" --> <div>Status</div>)
  }

}
