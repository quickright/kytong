package models

import java.util.Date

import reactivemongo.bson.BSONObjectID
import play.api.data.Forms._
/**
 * Created by chunli
 * Email:24228685@qq.com
 * Date:2015-03-13.
 * Time:下午3:32
 */

case class Employee(_id:BSONObjectID,name:String,name_py:String,mobile:String,email:String,school:String,edu:String,address:String,address_tel:String,sex:String,status:String,dob:Date,joiningDate:Date,remarks:String)

object EmployeeStatus extends Enumeration {
  val ZZ=Value("在职")
  val XJ=Value("休假")
  val LZ=Value("离职")
}

case class KyShop(_id:BSONObjectID,mobile:String,name:String,namePy:String,forShort:Option[String],address:String,email:Option[String],tel:Option[String],fax:Option[String],site:Option[String],status:Option[String],joiningDate:Option[Date],modifyDate:Option[Date],profile:Option[String],remarks:Option[String])

object KyShopStatus extends Enumeration {
  val ZC=Value("正常")
  val TY=Value("停用")
}

object Sex extends Enumeration {
  val man=Value("男")
  val woman=Value("女")
}
case class Page[A](items:Seq[A],page:Int,offset:Long,total:Long){
  lazy val prev = Option(page-1).filter(_ >=0)
  lazy val next = Option(page+1).filter( _ => (offset+items.size) < total )
}

object JsonFormats {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.modules.reactivemongo.json.BSONFormats._

  implicit val employeeFormat = Json.format[Employee]
  implicit val userFormat=Json.format[User]
  implicit val KyShopFormat=Json.format[KyShop]

}