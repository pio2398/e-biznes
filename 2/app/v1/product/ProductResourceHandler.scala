package v1.product

import javax.inject.{Inject, Provider}

import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * DTO for displaying test information.
  */
case class ProductResource(id: String, link: String, title: String, body: String)

object ProductResource {
  /**
    * Mapping to read/write a TestResource out as a JSON value.
    */
    implicit val format: Format[ProductResource] = Json.format
}


/**
  * Controls access to the backend data, returning [[ProductResource]]
  */
class TestResourceHandler @Inject()(
                                     routerProvider: Provider[ProductRouter],
                                     testRepository: ProductRepository)(implicit ec: ExecutionContext) {

  def create(testInput: TestFormInput)(
      implicit mc: MarkerContext): Future[ProductResource] = {
    val Ids = testRepository.productList.map { el => el.id.underlying}
    // generated new unique id for test
    val data = ProductData(ProductId(s"${Ids.max + 1}"), testInput.title, testInput.body)
    testRepository.create(data).map { id =>
      createTestResource(data)
    }
  }

  def lookup(id: String)(
      implicit mc: MarkerContext): Future[Option[ProductResource]] = {
    val productFuture = testRepository.get(ProductId(id))
    productFuture.map { maybeTestData =>
      maybeTestData.map { testData =>
        createTestResource(testData)
      }
    }
  }

  def delete(id: String)(
      implicit mc: MarkerContext): Future[Option[ProductResource]] = {
    val testFuture = testRepository.delete(ProductId(id))
    testFuture.map { maybeTestData =>
      maybeTestData.map { testData =>
        createTestResource(testData)
      }
    }
  }

  def update(id: String, testInput: TestFormInput)(
      implicit mc: MarkerContext): Future[Option[ProductResource]] = {
    val data = ProductData(ProductId(id), testInput.title, testInput.body)
    testRepository.update(ProductId(id), data).map { maybeTestData =>
      maybeTestData.map { testData =>
        createTestResource(testData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[ProductResource]] = {
    testRepository.list().map { testDataList =>
      testDataList.map(testData => createTestResource(testData))
    }
  }

  private def createTestResource(p: ProductData): ProductResource = {
    ProductResource(p.id.toString, routerProvider.get.link(p.id), p.title, p.body)
  }

}
