package v1.user

import javax.inject.Inject

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the UserResource controller.
  */
class UserRouter @Inject()(controller: UserController) extends SimpleRouter {
  val prefix = "/v1/users"
  
  def link(id: UserId): String = {
    import io.lemonlabs.uri.dsl._
    val url = prefix / id.toString
    url.toString()
  }

  override def routes: Routes = {
    case GET(p"/") =>
      controller.index

    case POST(p"/") =>
      controller.process

    case GET(p"/$id") =>
      controller.show(id)
    
    case DELETE(p"/$id") =>
      controller.delete(id)
    
    case POST (p"/$id/update") =>
      controller.update(id)
  
  }

}
