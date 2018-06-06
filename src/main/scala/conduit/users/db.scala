package conduit.users

import conduit.users.types._
import conduit.implicits._
// import tsec.authentication.BackingStore
import doobie._
import doobie.implicits._

package object db {

  object users {
    object queries {
      def findByEmail(email: Email): ConnectionIO[User] =
        sql"""
          select id, email, username, hashed_password, bio, image from users
          where email = $email
        """.query[User].unique

      def create(
          email: Email,
          username: Username,
          hashedPassword: HashedPassword,
      ): ConnectionIO[User] =
        sql"""
          insert into users (email, username, hashed_password)
          values ($email, $username, $hashedPassword)
        """.update.returningUser

      def update(user: User): ConnectionIO[User] =
        sql"""
          update users
          set email = ${user.email},
            username = ${user.username},
            hashed_password = ${user.hashedPassword},
            bio = ${user.bio},
            image = ${user.image}
          where id = ${user.id}
        """.update.returningUser

      private implicit class ReturningUser(self: Update0) {
        def returningUser: ConnectionIO[User] =
          self.withUniqueGeneratedKeys("id", "email", "username", "hashed_password", "bio", "image")
      }
    }
  }

}

class DoobieUserStore() {}

class DoobieTokenStore() {}
