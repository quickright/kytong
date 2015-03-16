package services

/**
 * Created by chunli
 * Email:24228685@qq.com
 * Date:2015-03-12.
 * Time:下午3:32
 */
import javax.inject.Singleton
import java.util.UUID

/**
 * A type declaring the interface that will be injectable.
 */
abstract class UUIDGenerator() {
  def generate: UUID
}

/**
 * A simple implementation of UUIDGenerator that we will inject.
 */
@Singleton
class SimpleUUIDGenerator extends UUIDGenerator {
  def generate: UUID = UUID.randomUUID()
}