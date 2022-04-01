import javax.inject._
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}
import v1.post._
import v1.product.{ProductRepository, ProductRepositoryImpl}
import v1.user.{UserRepository, UserRepositoryImpl}

/**
  * Sets up custom components for Play.
  *
  * https://www.playframework.com/documentation/latest/ScalaDependencyInjection
  */
class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule {

  override def configure() = {
    bind[PostRepository].to[PostRepositoryImpl].in[Singleton]()
    bind[ProductRepository].to[ProductRepositoryImpl].in[Singleton]()
    bind[UserRepository].to[UserRepositoryImpl].in[Singleton]()

  }
}
