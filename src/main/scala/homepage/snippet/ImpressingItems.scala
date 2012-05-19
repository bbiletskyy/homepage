package homepage.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import homepage.model.ImpressingItem
import net.liftweb.common.Empty
import net.liftweb.common.Box
import net.liftweb.http._
import net.liftweb.util._
import Helpers._
import scala.xml._
import net.liftweb.common.Full
import net.liftweb.http.SHtml._
import scala.util.Random


class ImpressingItems {
  
  
  
  def render = ImpressingItem.findAll.sortBy(i => Random.nextInt).map(i =>
    <div>
      <strong>{ i.title }</strong><br/>
      { i.content }<br/>
      { links(i) }{ videos(i) }
      <br/>
    </div>).toSeq 

  private def links(i: ImpressingItem): NodeSeq = if (i.urls.isEmpty() || i.urls.is.isEmpty()) NodeSeq.Empty else {
    val tu = i.urls.is.replace("[", "").replace("]", "").split('|')
    if (tu.size != 2) <span>{ tu.mkString(" ") }</span> else
      <span>
        <a href={ tu(1) }>{ tu(0) }</a>
      </span><br/>
  }

  private def videos(i: ImpressingItem): NodeSeq = {
    def id2html(id: String) =
      <a href={ "http://www.youtube.com/watch?v=" + id }>
        <img src={ "http://img.youtube.com/vi/" + id + "/2.jpg" } width={ "120" } height={ "90" } vspace={ "3" } hspace={ "3" } alt={ "Click to watch video" } title={ "Click to watch video" }/>
      </a>
    if (i.youtubeIds.isEmpty() || i.youtubeIds.is.isEmpty()) NodeSeq.Empty else
      i.youtubeIds.is.split(";").map(id => id2html(id)).toSeq ++ <br/>
  }

  

}