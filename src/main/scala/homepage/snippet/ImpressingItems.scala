package homepage.snippet

import scala.util.Random
import scala.xml._
import scala.xml.NodeSeq
import homepage.model.ImpressingItem
import net.liftweb.common.Box
import net.liftweb.common.Empty
import net.liftweb.http.SHtml._
import net.liftweb.http.S._
import net.liftweb.util.Helpers._
import net.liftweb.util._
import net.liftweb.http.SHtml
import net.liftweb.common.Full
import net.liftweb.http.RequestVar
import net.liftweb.mapper.By

class ImpressingItems {
  val ALL_TAGS = "All tags"
  object currentTagFilter extends RequestVar(Full(ALL_TAGS))

  def render = currentTagFilter.open_! match {
    case ALL_TAGS => ImpressingItem.findAll.sortBy(i => Random.nextInt).map(itemXhtml(_))
    case _ => ImpressingItem.findAll(By(ImpressingItem.tag, currentTagFilter.open_!)).map(itemXhtml(_))
  }

  private def itemXhtml(i: ImpressingItem): NodeSeq =
    <div>
      <strong>{ i.title }</strong><br/>
      { i.content }<br/>
      { linksXhtml(i) }{ videosXhtml(i) }
      <br/>
    </div>

  private def linksXhtml(i: ImpressingItem): NodeSeq = if (i.urls.isEmpty() || i.urls.is.isEmpty()) NodeSeq.Empty else {
    val tu = i.urls.is.filter(c => c != '[' && c != ']').split('|')
    if (tu.size != 2) <span>{ tu.mkString(" ") }</span> else
      <span>
        <a href={ tu(1) }>{ tu(0) }</a>
      </span><br/>
  }

  private def videosXhtml(i: ImpressingItem): NodeSeq = {
    def id2html(id: String) =
      <a href={ "http://www.youtube.com/watch?v=" + id }>
        <img src={ "http://img.youtube.com/vi/" + id + "/2.jpg" } width={ "120" } height={ "90" } vspace={ "3" } hspace={ "3" } alt={ "Click to watch video" } title={ "Click to watch video" }/>
      </a>
    if (i.youtubeIds.isEmpty() || i.youtubeIds.is.isEmpty()) NodeSeq.Empty else
      i.youtubeIds.is.split(";").map(id => id2html(id)).toSeq ++ <br/>
  }

  def filter(xhtml: NodeSeq): NodeSeq = {
    val tags = ALL_TAGS :: ImpressingItem.findAll().map(i => if (!i.tag.isEmpty) i.tag.is else "").removeDuplicates.filter(!_.isEmpty)
    bind("f", xhtml,
      "tag" -> selectObj[String](tags.map(t => (t, t)), currentTagFilter, selected => currentTagFilter(Full(selected))),
      "submit" -> submit("Apply", () => {}))
  }

}