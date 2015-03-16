package controllers.admin

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.Future
import reactivemongo.api.Cursor
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import org.slf4j.{LoggerFactory, Logger}
import javax.inject.Singleton
import play.api.mvc._
import play.api.libs.json._



import scala.concurrent.Future

/**
 * Created by chunli
 * Email:24228685@qq.com
 * Date:2015-03-12.
 * Time:下午2:55
 */
@Singleton
class Users  extends Controller with MongoController {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Users])

  def collection:JSONCollection = db.collection[JSONCollection]("users")

  import models._
  import models.JsonFormats._
  def registerUser = Action.async(parse.json) {
      request =>
      request.body.validate[User].map{
        user =>
          collection.insert(user).map{
            lastError =>
              logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(s"User Created")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
}
