package conduit.interpreter

import cats.data.OptionT
import cats.implicits._
import cats.effect.Sync
import scala.collection.mutable
import tsec.authentication.BackingStore

object InMemory {

  def backingStore[F[_], I, V](getId: V => I)(implicit F: Sync[F]): BackingStore[F, I, V] =
    new BackingStore[F, I, V] {
      private val store = mutable.HashMap.empty[I, V]

      override def put(elem: V): F[V] =
        store
          .put(getId(elem), elem)
          .fold(elem.pure[F])(Function.const(F.raiseError(new IllegalArgumentException)))

      override def get(id: I): OptionT[F, V] =
        OptionT.fromOption(store.get(id))

      override def update(v: V): F[V] = {
        store.update(getId(v), v)
        v.pure[F]
      }

      override def delete(id: I): F[Unit] =
        store
          .remove(id)
          .fold(F.raiseError(new IllegalArgumentException).void)(Function.const(F.unit))

    }

}
