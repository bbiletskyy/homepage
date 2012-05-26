package bootstrap.liftweb

import homepage.model.ImpressingItem
import homepage.model.Message
import homepage.model.Paper
import homepage.model.Talk
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import net.liftweb.common.Full
import net.liftweb.db.DB1.db1ToDb
import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.provider.HTTPRequest
import net.liftweb.http.LiftRules
import net.liftweb.http.S
import net.liftweb.mapper.DefaultConnectionIdentifier
import net.liftweb.mapper.StandardDBVendor
import net.liftweb.mapper.DB
import net.liftweb.mapper.Schemifier
import net.liftweb.sitemap.Loc.LinkText.strToLinkText
import net.liftweb.sitemap.LocPath.stringToLocPath
import net.liftweb.sitemap.Loc.Link
import net.liftweb.sitemap.Loc
import net.liftweb.sitemap.Menu
import net.liftweb.sitemap.SiteMap
import net.liftweb.util.Mailer
import net.liftweb.util.Props
import Boot._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
            "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // where to search snippet
    LiftRules.addToPackages("homepage")
    Schemifier.schemify(true, Schemifier.infoF _, ImpressingItem, Paper, Talk, Message)

    def sitemap() = SiteMap(
      Menu(loc("Impressing", "index" :: Nil)),
      Menu(loc("Research", "static" :: "research" :: "index" :: Nil),
        Menu(loc("Examples", "static" :: "research" :: "example" :: "index" :: Nil), 
        	Menu(loc("BZ", "static" :: "research" :: "example" :: "BZ" :: Nil)),
        	Menu(loc("Foraging", "static" :: "research" :: "example" :: "Foraging" :: Nil))
        )
      ),
      Menu(Loc("Publications", Link("publications" :: Nil, true, "/publications/index.do"), "Publications"), 
    	Menu(loc("Papers", "publications" :: "index" :: Nil)),
    	Menu(loc("Talks", "publications" :: "talks" :: Nil))
      ),
      Menu(loc("About", "static" :: "aboutmyself" :: Nil)),
      Menu(loc("Contact", "contact" :: Nil)))

    LiftRules.setSiteMapFunc(() => sitemap())

    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    //LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    S.addAround(DB.buildLoanWrapper)

    Mailer.authenticator = for {
      user <- Props.get("mail.user")
      pass <- Props.get("mail.password")
    } yield new Authenticator {
      override def getPasswordAuthentication =
        new PasswordAuthentication(user, pass)
    }
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

}

object Boot {
  /** A shortcut function */
  def loc(name: String, path: List[String]) = Loc(name, Link(path, true, path.mkString("/", "/", ".do")), name)
}

