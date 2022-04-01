package v1.user

import javax.inject.{Inject, Provider}

import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * DTO for displaying user information.
  */
case class UserResource(id: String, link: String, title: String, body: String)

object UserResource {
  /**
    * Mapping to read/write a UserResource out as a JSON value.
    */
    implicit val format: Format[UserResource] = Json.format
}


/**
  * Controls access to the backend data, returning [[UserResource]]
  */
class UserResourceHandler @Inject()(
    routerProvider: Provider[UserRouter],
    userRepository: UserRepository)(implicit ec: ExecutionContext) {

  def create(userInput: UserFormInput)(
      implicit mc: MarkerContext): Future[UserResource] = {
    val Ids = userRepository.userList.map {el => el.id.underlying}
    // generated new unique id for user
    val data = UserData(UserId(s"${Ids.max + 1}"), userInput.title, userInput.body)
    userRepository.create(data).map { id =>
      createUserResource(data)
    }
  }

  def lookup(id: String)(
      implicit mc: MarkerContext): Future[Option[UserResource]] = {
    val userFuture = userRepository.get(UserId(id))
    userFuture.map { maybeUserData =>
      maybeUserData.map { userData =>
        createUserResource(userData)
      }
    }
  }

  def delete(id: String)(
      implicit mc: MarkerContext): Future[Option[UserResource]] = {
    val userFuture = userRepository.delete(UserId(id))
    userFuture.map { maybeUserData =>
      maybeUserData.map { userData =>
        createUserResource(userData)
      }
    }
  }

  def update(id: String, userInput: UserFormInput)(
      implicit mc: MarkerContext): Future[Option[UserResource]] = {
    val data = UserData(UserId(id), userInput.title, userInput.body)
    userRepository.update(UserId(id), data).map { maybeUserData =>
      maybeUserData.map { userData =>
        createUserResource(userData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[UserResource]] = {
    userRepository.list().map { userDataList =>
      userDataList.map(userData => createUserResource(userData))
    }
  }

  private def createUserResource(p: UserData): UserResource = {
    UserResource(p.id.toString, routerProvider.get.link(p.id), p.title, p.body)
  }

}
