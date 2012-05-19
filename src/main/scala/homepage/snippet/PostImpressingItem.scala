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
  object urlsReqVar extends RequestVar(Full(""))
  object tagReqVar extends RequestVar(Full(""))

  def add(xhtml: NodeSeq): NodeSeq = {
    if (validateReqVars) {
      val ii: ImpressingItem = ImpressingItem.create
      ii.title(titleReqVar.open_!).content(contentReqVar.open_!).youtubeIds(youtubeIdsReqVar.open_!).tag(tagReqVar.open_!).save
    }
    bind("ii", xhtml,
      "title" --> text("Title", v => titleReqVar(Full(v))) % ("size" -> "50") % ("id" -> "title"),
      "content" --> text("", v => contentReqVar(Full(v))) % ("size" -> "50") % ("id" -> "content"),
      "youtubeIds" --> text("", v => youtubeIdsReqVar(Full(v))) % ("size" -> "50") % ("id" -> "youtubeIds"),
      "urls" --> text("", v => urlsReqVar(Full(v))) % ("size" -> "50") % ("id" -> "urls"),
      "tag" --> text("", v => tagReqVar(Full(v))) % ("size" -> "32") % ("id" -> "tag"),
      "submit" --> submit(?("Add Impressing Item"), () => {}))
  }

  private def validateReqVars: Boolean = {
    def isValid(v: RequestVar[Full[String]]) = !(v.isEmpty || v.open_!.length() == 0)
    
    isValid(titleReqVar) && isValid(contentReqVar) && isValid(tagReqVar)
  }

  //def impressingItems(xhtml: NodeSeq): NodeSeq = ImpressingItem.findAll.map(i => <div><strong>{ i.title }</strong> <br /> { i.content } <br /> { i.youtubeIds } { i.urls } - { i.tag } </div>).toSeq
}