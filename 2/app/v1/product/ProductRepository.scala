package v1.product

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future
import scala.collection.mutable.ListBuffer

final case class ProductData(id: ProductId, title: String, body: String)

class ProductId private(val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object ProductId {
  def apply(raw: String): ProductId = {
    require(raw != null)
    new ProductId(Integer.parseInt(raw))
  }
}

class TestExecutionContext @Inject()(actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the TestRepository.
  */
trait ProductRepository {
  def create(data: ProductData)(implicit mc: MarkerContext): Future[ProductId]

  // show all
  def list()(implicit mc: MarkerContext): Future[Iterable[ProductData]]

  def get(id: ProductId)(implicit mc: MarkerContext): Future[Option[ProductData]]
  def delete(id: ProductId)(implicit mc: MarkerContext): Future[Option[ProductData]]
  def update(id: ProductId, data: ProductData)(implicit mc: MarkerContext): Future[Option[ProductData]]

  val productList: ListBuffer[ProductData]
}

/**
  * A trivial implementation for the Test Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class ProductRepositoryImpl @Inject()()(implicit ec: TestExecutionContext)
    extends ProductRepository {

  private val logger = Logger(this.getClass)

  val productList = ListBuffer(
    ProductData(ProductId("1"), "title 1", "blog test 1"),
    ProductData(ProductId("2"), "title 2", "blog test 2"),
    ProductData(ProductId("3"), "title 3", "blog test 3"),
    ProductData(ProductId("4"), "title 4", "blog test 4"),
    ProductData(ProductId("5"), "title 5", "blog test 5")
  )

  override def list()(
      implicit mc: MarkerContext): Future[Iterable[ProductData]] = {
    Future {
      logger.trace(s"list: ")
      productList
    }
  }

  override def get(id: ProductId)(
      implicit mc: MarkerContext): Future[Option[ProductData]] = {
    Future {
      logger.trace(s"get: id = $id")
      productList.find(test => test.id == id)
    }
  }

  def create(data: ProductData)(implicit mc: MarkerContext): Future[ProductId] = {
    Future {
      logger.trace(s"create: data = $data")
      productList += data
      data.id
    }
  }

  def delete(id: ProductId)(implicit mc: MarkerContext): Future[Option[ProductData]] = {
    Future {
      logger.trace(s"delete: id = $id")

      var index = productList.indexWhere(test => test.id == id)
      var out: ProductData = null
      if(index != -1) {
       out = productList.remove(index)
      }
      Option(out)
    }

  }

  override def update(id: ProductId, data: ProductData)(
      implicit mc: MarkerContext): Future[Option[ProductData]] = {
    Future {
      logger.trace(s"update: id = $id")
      logger.trace(s"update: data = $data")

      val index = productList.indexWhere(test => test.id == id)
      var out: ProductData = null
      if(index != -1) {

       out = ProductData(productList.remove(index).id, data.title, data.body)
       productList += out
      }
      Option(out)
    }
  }

}
