package controllers.base

import play.api.mvc.Controller
import reactivemongo.api._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection

/**
 * Created by chunli
 * Email:24228685@qq.com
 * Date:2015-02-13.
 * Time:下午6:33
 */
trait MgController extends Controller  with MongoController{

}

