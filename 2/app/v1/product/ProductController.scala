package v1.product

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class TestFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class ProductController @Inject()(cc: ProductControllerComponents)(
    implicit ec: ExecutionContext)
    extends ProductBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[TestFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "body" -> text
      )(TestFormInput.apply)(TestFormInput.unapply)
    )
  }

  private val updateForm: Form[TestFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> default(text, ""),
        "body" -> default(text, "")
      )(TestFormInput.apply)(TestFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = TestAction.async { implicit request =>
    logger.trace("index: ")
    testResourceHandler.find.map { tests =>
      Ok(Json.toJson(tests))
    }
  }

  def process: Action[AnyContent] = TestAction.async { implicit request =>
    logger.trace("process: ")
    processJsonTest()
  }

  def show(id: String): Action[AnyContent] = TestAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      testResourceHandler.lookup(id).map { test =>
        Ok(Json.toJson(test))
      }
  }

  def delete(id: String): Action[AnyContent] = TestAction.async {
    implicit request =>
      logger.trace(s"delete: id = $id")
      testResourceHandler.delete(id).map { test =>
        Ok(Json.toJson(test))
      }
  }

  def update(id: String): Action[AnyContent] = TestAction.async { implicit request =>
    logger.trace(s"update: id = $id")
    processJsonTestUpdate(id)
  }


  private def processJsonTest[A]()(
      implicit request: ProductRequest[A]): Future[Result] = {

    def failure(badForm: Form[TestFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: TestFormInput) = {
      testResourceHandler.create(input).map { test =>
        Created(Json.toJson(test)).withHeaders(LOCATION -> test.link)
      }
    }
    
    form.bindFromRequest().fold(failure, success)
  }


  private def processJsonTestUpdate[A](id: String)(
      implicit request: ProductRequest[A]): Future[Result] = {

    def failure(badForm: Form[TestFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: TestFormInput) = {
      testResourceHandler.update(id, input).map { test =>
        Created(Json.toJson(test))
      }
    }
    import play.api.data.Forms._

    updateForm.bindFromRequest().fold(failure, success)
  }

}
