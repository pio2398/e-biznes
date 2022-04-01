package v1.user

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class UserFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class UserController @Inject()(cc: UserControllerComponents)(
    implicit ec: ExecutionContext)
    extends UserBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[UserFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "body" -> text
      )(UserFormInput.apply)(UserFormInput.unapply)
    )
  }

  private val updateForm: Form[UserFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> default(text, ""),
        "body" -> default(text, "")
      )(UserFormInput.apply)(UserFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace("index: ")
    userResourceHandler.find.map { users =>
      Ok(Json.toJson(users))
    }
  }

  def process: Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace("process: ")
    processJsonUser()
  }

  def show(id: String): Action[AnyContent] = UserAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      userResourceHandler.lookup(id).map { user =>
        Ok(Json.toJson(user))
      }
  }

  def delete(id: String): Action[AnyContent] = UserAction.async {
    implicit request =>
      logger.trace(s"delete: id = $id")
      userResourceHandler.delete(id).map { user =>
        Ok(Json.toJson(user))
      }
  }

  def update(id: String): Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace(s"update: id = $id")
    processJsonUserUpdate(id)
  }


  private def processJsonUser[A]()(
      implicit request: UserRequest[A]): Future[Result] = {

    def failure(badForm: Form[UserFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: UserFormInput) = {
      userResourceHandler.create(input).map { user =>
        Created(Json.toJson(user)).withHeaders(LOCATION -> user.link)
      }
    }
    
    form.bindFromRequest().fold(failure, success)
  }


  private def processJsonUserUpdate[A](id: String)(
      implicit request: UserRequest[A]): Future[Result] = {

    def failure(badForm: Form[UserFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: UserFormInput) = {
      userResourceHandler.update(id, input).map { user =>
        Created(Json.toJson(user))
      }
    }
    import play.api.data.Forms._

    updateForm.bindFromRequest().fold(failure, success)
  }

}
