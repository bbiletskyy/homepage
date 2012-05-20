package homepage.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.RequestVar
import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import homepage.model.Paper
import net.liftweb.mapper.OrderBy
import net.liftweb.mapper.Descending

class Papers {

  def showPapers(xhtml: NodeSeq): NodeSeq =
    Paper.findAll(OrderBy(Paper.year, Descending)).map { p =>
      <div>
        <strong>{ p.title.is }</strong> ({ p.year.is }) <br/>
        <i>{ p.authors.is }</i> at {journalXhtml(p)}<br/><br/>
      </div>
    }.toSeq

  
  def journalXhtml(p: Paper): NodeSeq = if (p.url.isEmpty() || p.url.is.isEmpty()) <span>{ p.journal.is } </span> else 
  	<a href={p.url.is} alt={ "Go to paper page on publisher's site" } title={ "Go to paper page on publisher's site" }>{ p.journal.is }</a>
   
    
    
  //  def addPaper(xhtml: NodeSeq): NodeSeq = {
  //    object authorsReqVar extends RequestVar(Full(""))
  //    object titleReqVar extends RequestVar(Full(""))
  //    object journalReqVar extends RequestVar(Full(""))
  //    object tagReqVar extends RequestVar(Full(""))
  //    object urlReqVar extends RequestVar(Full(""))
  //    object yearReqVar extends RequestVar(Full(""))
  //    def isEmpty(r: RequestVar[Full[String]]): Boolean = r.isEmpty || r.open_!.length == 0
  //    def fieldsValid = !(isEmpty(authorsReqVar) || isEmpty(titleReqVar) || isEmpty(journalReqVar) || isEmpty(tagReqVar) || isEmpty(urlReqVar) || isEmpty(yearReqVar))
  //
  //    if (fieldsValid) {
  //      val paper: Paper = Paper.create
  //      paper.authors(authorsReqVar.open_!).title(titleReqVar.open_!).tag(tagReqVar.open_!).url(urlReqVar.open_!).year(yearReqVar.open_!).save
  //    }
  //
  //    bind("ap", xhtml,
  //      "authors" --> text("", v => authorsReqVar(Full(v))) % ("size" -> "50") % ("id" -> "authors"),
  //      "title" --> text("", v => titleReqVar(Full(v))) % ("size" -> "50") % ("id" -> "title"),
  //      "journal" --> text("", v => journalReqVar(Full(v))) % ("size" -> "50") % ("id" -> "journal"),
  //      "tag" --> text("", v => tagReqVar(Full(v))) % ("size" -> "50") % ("id" -> "tag"),
  //      "url" --> text("", v => urlReqVar(Full(v))) % ("size" -> "50") % ("id" -> "url"),
  //      "year" --> text("", v => yearReqVar(Full(v))) % ("size" -> "4") % ("id" -> "year"),
  //      "submit" --> submit(?("Add Paper"), () => {}),
  //      "messages" --> <div>Status</div>)
  //  }

}
