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

  def add(xhtml: NodeSeq): NodeSeq = {
    val titleOk = !(titleReqVar.isEmpty || titleReqVar.open_!.length == 0)
    val contentOk = !(contentReqVar.isEmpty || contentReqVar.open_!.length == 0)
    if (titleOk && contentOk) {
      val ii: ImpressingItem = ImpressingItem.create
      ii.title(titleReqVar.open_!).content(contentReqVar.open_!).save
    }
    bind("ii", xhtml,
      "title" --> text("", v => titleReqVar(Full(v))) % ("size" -> "50") % ("id" -> "title"),
      "content" --> text("", v => contentReqVar(Full(v))) % ("size" -> "50") % ("id" -> "content"),
      "submit" --> submit(?("Add Impressing Item"), () => {}))
  }

  def impressingItems(xhtml: NodeSeq): NodeSeq = {
    //val xxx = ImpressingItem.findAll
    //val nn = NodeSeq.fromSeq(Seq(<p>123</p>, <p>321</p>))

    //val res = ImpressingItem.findAll

    val xml = ImpressingItem.findAll.map(i => <p>{ i.title } -- { i.content }</p>).toSeq
    xml
  }

  //  def show(xhtml: NodeSeq): NodeSeq = {
  //    val msgSent = !(qpx.isEmpty || qpx.open_!.length == 0)
  //    if (msgSent) {
  //      val msg: ImpressingItem = ImpressingItem.create
  //      msg.title(qpx.open_!).save
  //    }
  //    val impressingItems = ImpressingItem.findAll
  //    val temp = impressingItems.foldLeft("") { (str, item) => str + " ### " + item.title }
  //    bind("qp", xhtml,
  //      "update" --> text("", v => qpx(Full(v))) % ("size" -> "50") % ("id" -> "update"),
  //      "submit" --> submit(?("Update"), () => {}),
  //      "messages" --> <div>{ temp }</div>)
  //  }
}