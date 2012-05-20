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

/** Renders  the papers*/
class Papers {
  def showPapers(xhtml: NodeSeq): NodeSeq =
    Paper.findAll(OrderBy(Paper.year, Descending)).map { p =>
      <div>
        <strong>{ p.title.is }</strong>&nbsp;({ p.year.is })<br/>
        <i>{ p.authors.is }</i> at {journalXhtml(p)}<br/><br/>
      </div>
    }.toSeq
  
  def journalXhtml(p: Paper): NodeSeq = if (p.url.isEmpty() || p.url.is.isEmpty()) <span>{ p.journal.is } </span> else 
  	<a href={p.url.is} alt={ "Go to paper page on publisher's site" } title={ "Go to paper page on publisher's site" }>{ p.journal.is }</a>
 }
