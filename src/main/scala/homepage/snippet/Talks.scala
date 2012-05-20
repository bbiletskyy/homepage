package homepage.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.RequestVar
import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import homepage.model.Paper
import homepage.model.Talk
import net.liftweb.mapper.OrderBy
import net.liftweb.mapper.Descending

class Talks {

  def showTalks(xhtml: NodeSeq): NodeSeq = Talk.findAll(OrderBy(Talk.year, Descending)).map { t =>
      <div>
        <strong>{ t.title.is }</strong>&nbsp;({t.year.is })<br/>
        <i>{ t.authors.is }</i> at { conferenceXhtml(t) }<br/><br/>
      </div>
    }.toSeq

  def conferenceXhtml(t: Talk) = if (t.url.isEmpty() || t.url.is.isEmpty()) <span>{ t.conference.is }</span> else
    <a href={ t.url.is } alt={ "Go to conference site" } title={ "Go to conference site" }>{ t.conference.is }</a>
}
