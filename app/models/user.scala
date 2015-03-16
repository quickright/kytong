package models



/**
 * Created by chunli
 * Email:24228685@qq.com
 * Date:2015-03-12.
 * Time:下午3:05
 */

case class User(mobile:String,
                 ent_name:String,
                 ent_name_py:String,
                 ent_short_name:String,
                 address:String,
                 email:String,
                 tel:String,
                 fax:String,
                 site:String,
                 remark:String,
                 status:String,
                 create_date:String,
                 modify_date:String)


object ceshi extends App{
  print(System.currentTimeMillis())
  import play.api.libs.json.Json
  print(Json.obj("_id" -> Json.obj("$oid" -> "fdsfsdfds")).toString())
}