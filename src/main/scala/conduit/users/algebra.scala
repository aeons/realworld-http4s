package conduit.users

import conduit.users.types._

trait UserAlgebra[F[_]] {
  import UserAlgebra._

  def findById(id: UserId): F[Option[User]]
  def findByEmail(email: Email): F[Option[User]]
  def findByUsername(username: Username): F[Option[User]]
  def create(email: Email, username: Username, password: String): F[Either[UserCreationError, User]]
  def update(email: Email, newEmail: Option[Email]): F[Option[User]]
}

object UserAlgebra {
  sealed trait UserCreationError extends Exception

  case object UsernameTaken extends UserCreationError
  case object EmailTaken    extends UserCreationError

  case object UserNotFound extends Exception
}

trait TokenAlgebra[F[_]] {
  def findByUserId(id: UserId): F[Option[JwtToken]]
  def create(id: UserId): F[JwtToken]
}

trait ProfileAlgebra[F[_]] {
  def findByUsername(finder: Option[User], username: Username): F[Option[Profile]]
  def follow(follower: User, followee: Username): F[Option[Profile]]
  def unfollow(follower: User, followee: Username): F[Option[Profile]]
  def isFollowing(follower: UserId, followee: UserId): F[Boolean]
}
