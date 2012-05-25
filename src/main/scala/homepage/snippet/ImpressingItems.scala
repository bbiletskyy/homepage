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
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds.RedirectTo
import net.liftweb.http.js.JsCmd

class ImpressingItems {
  val ALL_TAGS = "All"
  object currentTagFilter extends RequestVar(Full(ALL_TAGS))

  def filter = "name=tag" #> SHtml.ajaxSelect(tags.map(t => (t.toString, t.toString)), currentTagFilter, onSelect(_))
  
  def tags = ALL_TAGS :: ImpressingItem.findAll().map(i => if (!i.tag.isEmpty) i.tag.is else "").removeDuplicates.filter(!_.isEmpty)
  
  def items = currentTagFilter.open_! match {
    case ALL_TAGS => ImpressingItem.findAll.sortBy(i => Random.nextInt).map(itemXhtml(_))
    case _ => ImpressingItem.findAll(By(ImpressingItem.tag, currentTagFilter.open_!)).map(itemXhtml(_))
  }

  private def itemXhtml(i: ImpressingItem): NodeSeq =
    <div>
      <strong>{ i.title }</strong><br/>
      { i.content }<br/>
      { itemLinksXhtml(i) }{ itemVideosXhtml(i) }
      <br/>
    </div>

  private def itemLinksXhtml(i: ImpressingItem): NodeSeq = if (i.urls.isEmpty() || i.urls.is.isEmpty()) NodeSeq.Empty else
    /** split links, remove square brackets, split link label from url, create xhtml */
    i.urls.is.split(';').map(_.filter(c => c != '[' && c != ']').split('|')).map { lu => <span><a href={ lu(1) }>{ lu(0) }</a><br/></span> }.toSeq

  private def itemVideosXhtml(i: ImpressingItem): NodeSeq = {
    def id2html(id: String) =
      <a href={ "http://www.youtube.com/watch?v=" + id }>
        <img src={ "http://img.youtube.com/vi/" + id + "/2.jpg" } width={ "120" } height={ "90" } vspace={ "3" } hspace={ "3" } alt={ "Click to watch video" } title={ "Click to watch video" }/>
      </a>
    if (i.youtubeIds.isEmpty() || i.youtubeIds.is.isEmpty()) NodeSeq.Empty else
      i.youtubeIds.is.split(";").map(id => id2html(id)).toSeq ++ <br/>
  }

  private def onSelect(v: String): JsCmd = RedirectTo(S.referer openOr "/", () => {
    currentTagFilter(Full(v))
  })

}