package homepage.snippet
import net.liftweb.http.S

class Image {
  def render = {
    <span>{ S.uriAndQueryString + "; " + S.uri }</span>
    S.uri match {
      case s if s.contains("contact")   => <img src="/images/contacts.gif"/>
      case _ => <img src="/images/impressing.gif"/>
    }

  }
}

object Image {
  def main(args: Array[String]) {
    val s = "http://localhost:8080/contact.do"
    s match {
      case ss if ss.contains("contact")  => println("1")
      case _ => println("0")
    }
    println("Hello")
  }
}