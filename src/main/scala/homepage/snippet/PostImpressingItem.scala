package homepage.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.RequestVar
import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import homepage.model.ImpressingItem
import scala.xml.Node

class PostImpressingItem {
  object titleReqVar extends RequestVar(Full(""))
  object contentReqVar extends RequestVar(Full(""))
  object youtubeIdsReqVar extends RequestVar(Full(""))
  object tagReqVar extends RequestVar(Full(""))

  
  def add(xhtml: NodeSeq): NodeSeq = {
    if (validateReqVars) {
      val ii: ImpressingItem = ImpressingItem.create
      ii.title(titleReqVar.open_!).content(contentReqVar.open_!).youtubeIds(youtubeIdsReqVar.open_!).tag(youtubeIdsReqVar.open_!).save
    }
    bind("ii", xhtml,
      "title" --> text("Title", v => titleReqVar(Full(v))) % ("size" -> "50") % ("id" -> "title"),
      "content" --> text("", v => contentReqVar(Full(v))) % ("size" -> "50") % ("id" -> "content"),
      "youtubeIds" --> text("", v => youtubeIdsReqVar(Full(v))) % ("size" -> "50") % ("id" -> "youtubeIds"),
      "tag" --> text("", v => tagReqVar(Full(v))) % ("size" -> "32") % ("id" -> "tag"),
      "submit" --> submit(?("Add Impressing Item"), () => {}))
  }

  private def validateReqVars: Boolean = {
    val titleOk = !(titleReqVar.isEmpty || titleReqVar.open_!.length == 0)
    val contentOk = !(contentReqVar.isEmpty || contentReqVar.open_!.length == 0)
    val youtubeIdsOk = !(youtubeIdsReqVar.isEmpty || youtubeIdsReqVar.open_!.length == 0)
    val tagOk = !(tagReqVar.isEmpty || tagReqVar.open_!.length == 0)
    titleOk && contentOk && youtubeIdsOk && tagOk
  }

  def impressingItems(xhtml: NodeSeq): NodeSeq = ImpressingItem.findAll.map(i => <p>{ i.title } <br/> { i.content } - { i.youtubeIds } - { i.tag } </p>).toSeq
}