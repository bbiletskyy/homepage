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
import net.liftweb.sitemap.Loc.Link
import net.liftweb.sitemap.Loc
import net.liftweb.sitemap.Menu
import net.liftweb.sitemap.SiteMap
import net.liftweb.util.Mailer
import net.liftweb.util.Props

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
      Menu(Loc("Impressing", Link(List("index"), true, "/index.do"), "Impressing")),
      Menu(Loc("Research", Link("static" :: "research" :: Nil, true, "/static/research/index.do"), "Research"),
        Menu(Loc("Particlez", Link("static" :: "research" :: "particlez" :: Nil, true, "/static/research/index.do"), "Particlez")),
        Menu(Loc("Example1", Link("static" :: "research" :: "example1" :: Nil, true, "/static/research/example1.do"), "Example1")),
        Menu(Loc("Example2", Link("static" :: "research" :: "example2" :: Nil, true, "/static/research/example2.do"), "Example2"))),
      Menu(Loc("Publications", Link("publications" :: Nil, true, "/publications/index.do"), "Publications"),
        Menu(Loc("Papers", Link("publications" :: "papers" :: Nil, true, "/publications/index.do"), "Papers")),
        Menu(Loc("Talks", Link("publications" :: "talks" :: Nil, true, "/publications/talks.do"), "Talks"))),
      Menu(Loc("About", Link("static" :: "aboutmyself" :: Nil, true, "/static/aboutmyself.do"), "About")),
      Menu(Loc("Contact", Link("contact" :: Nil, true, "/contact.do"), "Contact")))

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
