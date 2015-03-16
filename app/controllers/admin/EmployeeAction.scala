package controllers.admin

import java.util.concurrent.TimeoutException

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
import views.html
import models.Employee
import models.JsonFormats.employeeFormat
import models.Page

/**
 * Created by chunli
 * Email:24228685@qq.com
 * Date:2015-03-13.
 * Time:下午3:30
 */
object EmployeeAction extends Controller with MongoController {

  implicit val timeout = 10.seconds
  //case class Employee(_id:BSONObjectID,name:String,name_py:String,mobile:String,email:String,school:String,edu:String,address:String,address_tel:String,sex:String,status:String,dob:Date,joiningDate:Date,remarks:String)

  val employeeForm = Form(
    mapping(
      "id" -> ignored(BSONObjectID.generate: BSONObjectID),
      "name" -> nonEmptyText,
      "name_py" -> nonEmptyText,
      "mobile" -> nonEmptyText,
      "email" -> text,
      "school" -> text,
      "edu" -> of[String],
      "address" -> nonEmptyText,
      "address_tel" -> text,
      "sex" -> text,
      "status" -> nonEmptyText,
      "dob" -> date("yyyy-MM-dd"),
      "joiningDate" -> date("yyyy-MM-dd hh:mm:ss"),
      "remarks" -> text)(Employee.apply)(Employee.unapply)
  )

  def collection: JSONCollection = db.collection[JSONCollection]("employees")

  import play.api.data.Form
  import models._
  import models.JsonFormats._


  def index = Action {Home}

  val Home = Redirect( controllers.admin.routes.EmployeeAction.list())

  def list(page: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
   val futurePage = filter.length > 0 match {
     case true => collection.find(Json.obj("name" ->filter )).cursor[Employee].collect[List]()
     case false => collection.genericQueryBuilder.cursor[Employee].collect[List]()
   }
    futurePage.map(employees => Ok(views.html.employee.list(Page(employees,0,10,20),orderBy,filter))).recover{
      case t:TimeoutException =>
        Logger.error("Problem found in employee list process")
        InternalServerError(t.getMessage)
    }
  }

  def edit(id: String) = Action.async {
    val futureEmp = collection.find(Json.obj("_id" -> Json.obj("$oid" -> id))).cursor[Employee].collect[List]()
    futureEmp.map {
      emps: List[Employee] => Ok(views.html.employee.editForm(id,employeeForm.fill(emps.head)))
    }.recover{
      case t: TimeoutException =>
        Logger.error("Problem found in employee edit process")
        InternalServerError(t.getMessage)
    }
  }

  def update(id:String) = Action.async{ implicit request =>
    employeeForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.employee.editForm(id,formWithErrors))),
      employee => {
        val futureUpdateEmp = collection.update(Json.obj("_id" -> Json.obj("$oid" -> id)),employee.copy(_id = BSONObjectID(id)))
        futureUpdateEmp.map{ result =>
          Home.flashing("success" -> s"Employee ${employee.name} has been updated")
        } .recover {
          case t:TimeoutException =>
            Logger.error("Problem found in employee edit process")
            InternalServerError(t.getMessage)
        }
      }
    )
  }

  def create = Action {
    Ok(views.html.employee.createForm(employeeForm))
  }

  def save = Action.async{ implicit request =>
    employeeForm.bindFromRequest.fold(
    formWithErrors => Future.successful(BadRequest(views.html.employee.createForm(formWithErrors))),
    employee => {
      val futureUpdateEmp = collection.insert(employee.copy(_id = BSONObjectID.generate))
      futureUpdateEmp.map{ result =>
        Home.flashing("success" -> s"Employee ${employee.name} has been created")
      }.recover{
        case t:TimeoutException =>
          Logger.error("Problem found in employee edit process")
          InternalServerError(t.getMessage)
      }
    }
    )
  }

  def delete(id: String) = Action.async{
    val futureInt = collection.remove(Json.obj("_id" -> Json.obj("$oid" -> id)),firstMatchOnly = true)
    futureInt.map( i => Home.flashing("success" -> "Employee has been deleted")).recover{
      case t:TimeoutException =>
        Logger.error("Problem found in employee edit process")
        InternalServerError(t.getMessage)
    }
  }
}
