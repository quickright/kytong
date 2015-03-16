package controllers

import play.api.libs.json._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import javax.inject.{Singleton, Inject}
import services.UUIDGenerator
import org.slf4j.{LoggerFactory, Logger}
import play.api.mvc._

@Singleton
class Application @Inject() (uuidGenerator: UUIDGenerator) extends Controller with MongoController { //extends MgController {

  def collection: JSONCollection = db.collection[JSONCollection]("persons")

  def category: JSONCollection = db.collection[JSONCollection]("category")

  def test: JSONCollection = db.collection[JSONCollection]("test")

  private final val logger:Logger = LoggerFactory.getLogger(classOf[Application])

  /*def index = LoggingAction { implicit request =>
    Ok("Got request [" + request + "]")
    //Ok(views.html.main)
  }*/

  def index = Action {
    logger.info("Serving index page ... ")
    Ok(views.html.index())
  }

  def randomUUID = Action {
    logger.info("calling UUIDGenerator...")
    Ok(uuidGenerator.generate.toString)
  }


  def create(name: String, age: Int) = Action.async {
    val json = Json.obj(
      "name" -> name,
      "age" -> age,
      "created" -> new java.util.Date().getTime())

    collection.insert(json).map(lastError =>
      Ok("Mongo LastError: %s".format(lastError)))
  }


  /*def createFromJson = Action.async(parse.json) { request =>
    import play.api.libs.json.Reads._
    /*
     * request.body is a JsValue.
     * There is an implicit Writes that turns this JsValue as a JsObject,
     * so you can call insert() with this JsValue.
     * (insert() takes a JsObject as parameter, or anything that can be
     * turned into a JsObject using a Writes.)
     */
    val transformer: Reads[JsObject] =
      Reads.jsPickBranch[JsString](__ \ "firstName") and
        Reads.jsPickBranch[JsString](__ \ "lastName") and
        Reads.jsPickBranch[JsNumber](__ \ "age") reduce

    request.body.transform(transformer).map { result =>
      collection.insert(result).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Created
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }*/
//
  def list = Action.async{
    val cursor: Cursor[JsObject] = category.find(Json.obj("status" -> 1)).sort(Json.obj("order" -> 1 )).cursor[JsObject]
    val futureCategoryList: Future[List[JsObject]] = cursor.collect[List]()

    val futureCategoryJsonList: Future[JsArray] = futureCategoryList.map{ category =>
      Json.arr(category)
    }
    // everything's ok! Let's reply with the array
    futureCategoryJsonList.map{ category =>
      Ok(category)
    }
  }

  def listtest = Action.async{
    val cursor: Cursor[JsObject] = test.find(Json.obj("name" -> "cc")).cursor[JsObject]
    val futureCategoryList: Future[List[JsObject]] = cursor.collect[List]()

    val futureCategoryJsonList: Future[JsArray] = futureCategoryList.map{ test =>
      Json.arr(test)
    }
    // everything's ok! Let's reply with the array
    futureCategoryJsonList.map{ test =>
      Ok(test)
    }
  }
  def findByName(name: String) = Action.async {
    // let's do our query
    val cursor: Cursor[JsObject] = collection.
      // find all people with name `name`
      find(Json.obj("name" -> name)).
      // sort them by creation date
      sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject]

    // gather all the JsObjects in a list
    val futurePersonsList: Future[List[JsObject]] = cursor.collect[List]()

    // transform the list into a JsArray
    val futurePersonsJsonArray: Future[JsArray] = futurePersonsList.map { persons =>
      Json.arr(persons)
    }

    // everything's ok! Let's reply with the array
    futurePersonsJsonArray.map { persons =>
      Ok(persons)
    }
  }
}
