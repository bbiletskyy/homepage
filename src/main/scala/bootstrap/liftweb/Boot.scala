package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper.{ DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, StandardDBVendor }
import _root_.java.sql.{ Connection, DriverManager }
import _root_.homepage.model._

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
    Schemifier.schemify(true, Schemifier.infoF _, Message, ImpressingItem, Talk)

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
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
}
