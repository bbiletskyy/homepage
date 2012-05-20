package homepage.snippet
import net.liftweb.http.S

class Image {
  def render = S.uri match {
    case s if s.contains("publications") => <img src="/images/publications.gif"/>
    case s if s.contains("research") => <img src="/images/research.gif"/>
    case s if s.contains("contact") => <img src="/images/contacts.gif"/>
    case _ => <img src="/images/impressing.gif"/>
  }
}

object Image {
 
}

