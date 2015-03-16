package controllers.admin

import java.util.concurrent.TimeoutException

import controllers.admin.EmployeeAction._
import models.{Page, Employee, KyShop}
import play.api.Logger
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.Form
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.BSONObjectID

/**
 * Created by chunli
 * Email:24228685@qq.com
 * Date:2015-03-16.
 * Time:上午9:39
 */
object KyShopAction extends Controller with MongoController {
  def collection: JSONCollection = db.collection[JSONCollection]("kyshop")
  import play.api.data.Form
  import models._
  import models.JsonFormats._
   val kyShopForm = Form(
    mapping(
      "id" -> ignored(BSONObjectID.generate: BSONObjectID),
      "mobile" ->nonEmptyText,
      "name" -> nonEmptyText,
      "namePy" -> nonEmptyText,
      "forShort" -> optional(text),
      "address" -> nonEmptyText,
      "email" -> optional(text),
      "tel" -> optional(text),
      "fax" -> optional(text),
      "site" -> optional(text),
      "status" -> optional(text),
      "joiningDate" -> optional(date("yyyy-MM-dd")),
      "modifyDate" -> optional(date("yyyy-MM-dd hh:mm:ss")),
      "profile" -> optional(text),
      "remarks" -> optional(text))(KyShop.apply)(KyShop.unapply)
  )


  def index =Action {
      Ok("Hello!")
    }// {Ok("index")}//Action {Home}

  val Home = { Ok("home")}//Redirect( controllers.admin.routes.KyShopAction.list())

  def list()={//page: Int, orderBy: Int, filter: String) ={
    /*Action.async { implicit request =>
    val futurePage = filter.length > 0 match {
      case true => collection.find(Json.obj("name" ->filter )).cursor[KyShop].collect[List]()
      case false => collection.genericQueryBuilder.cursor[KyShop].collect[List]()
    }*/
    Ok("hello")
   /* futurePage.map(kyshop => Ok("hello").recover{
      case t:TimeoutException =>
        Logger.error("Problem found in kyshop list process")
        InternalServerError(t.getMessage)
    }*/
    //views.html.admin.kyshop.list(Page(kyshop,0,10,20),orderBy,filter))
  }

  def save = Action { implicit request =>
    kyShopForm.bindFromRequest().fold(
       error=>Ok("error"),
       kyshop =>{
       Ok(kyshop.name)
        }
    )
  }

  def save1 = Action.async { implicit request =>
    Logger.info("save ceshi")
    kyShopForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.admin.kyshop.createForm(formWithErrors))),
      kyshop => {
        Logger.info(kyshop.name)
        val futureUpdateEmp = collection.insert(kyshop.copy(_id = BSONObjectID.generate))
        futureUpdateEmp.map{ result =>
         // Home.flashing("success" -> s" ${kyshop.name} 创建成功！")
          Ok(kyshop.name)
        }.recover{
          case t:TimeoutException =>
            Logger.error("Sorry")
            InternalServerError(t.getMessage)
        }
      }
    )
  }
  def create = Action {
    Ok(views.html.admin.kyshop.createForm(kyShopForm))
  }

}
