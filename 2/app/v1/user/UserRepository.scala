package v1.user

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future
import scala.collection.mutable.ListBuffer

final case class UserData(id: UserId, title: String, body: String)

class UserId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object UserId {
  def apply(raw: String): UserId = {
    require(raw != null)
    new UserId(Integer.parseInt(raw))
  }
}

class UserExecutionContext @Inject()(actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the UserRepository.
  */
trait UserRepository {
  def create(data: UserData)(implicit mc: MarkerContext): Future[UserId]

  // show all
  def list()(implicit mc: MarkerContext): Future[Iterable[UserData]]

  def get(id: UserId)(implicit mc: MarkerContext): Future[Option[UserData]]
  def delete(id: UserId)(implicit mc: MarkerContext): Future[Option[UserData]]
  def update(id: UserId, data: UserData)(implicit mc: MarkerContext): Future[Option[UserData]]
  
  val userList: ListBuffer[UserData]
}

/**
  * A trivial implementation for the User Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class UserRepositoryImpl @Inject()()(implicit ec: UserExecutionContext)
    extends UserRepository {

  private val logger = Logger(this.getClass)

  val userList = ListBuffer(
    UserData(UserId("1"), "title 1", "blog user 1"),
    UserData(UserId("2"), "title 2", "blog user 2"),
    UserData(UserId("3"), "title 3", "blog user 3"),
    UserData(UserId("4"), "title 4", "blog user 4"),
    UserData(UserId("5"), "title 5", "blog user 5")
  )

  override def list()(
      implicit mc: MarkerContext): Future[Iterable[UserData]] = {
    Future {
      logger.trace(s"list: ")
      userList
    }
  }

  override def get(id: UserId)(
      implicit mc: MarkerContext): Future[Option[UserData]] = {
    Future {
      logger.trace(s"get: id = $id")
      userList.find(user => user.id == id)
    }
  }

  def create(data: UserData)(implicit mc: MarkerContext): Future[UserId] = {
    Future {
      logger.trace(s"create: data = $data")
      userList += data
      data.id
    }
  }

  def delete(id: UserId)(implicit mc: MarkerContext): Future[Option[UserData]] = {
    Future {
      logger.trace(s"delete: id = $id")
      
      var index = userList.indexWhere(user => user.id == id)
      var out: UserData = null
      if(index != -1) {
       out = userList.remove(index)
      }
      Option(out)
    }

  }

  override def update(id: UserId, data: UserData)(
      implicit mc: MarkerContext): Future[Option[UserData]] = {
    Future {
      logger.trace(s"update: id = $id")
      logger.trace(s"update: data = $data")
      
      var index = userList.indexWhere(user => user.id == id)
      var out: UserData = null
      if(index != -1) {
       
       out = UserData(userList.remove(index).id, data.title, data.body)
       userList += out
      }
      Option(out)
    }
  }

}
