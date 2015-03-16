package controllers.base

import play.api.Logger
import play.api.mvc.{Result, Request, ActionBuilder}

import scala.concurrent.Future

/**
 * Created by chunli
 * Email:24228685@qq.com
 * Date:2015-02-13.
 * Time:下午6:30
 */
object LoggingAction extends ActionBuilder[Request]{
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    Logger.info("")
    block(request)
  }
}
