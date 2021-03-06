package functor.bifunctor

import cats.functor.Bifunctor

/* Translated into Scala examples illustrating BiFunctor from fun, clear explanation by:
  George Wilson - The Extended Functor Family: https://www.youtube.com/watch?v=JUVMiRRq6wU

  - BiFunctor allow you to perform two different operations

  Haskell Bifunctor - type class & laws

  class Bifunctor p where
    bimap :: (a -> b) -> (x -> y) -> p a x -> p b y

    first ::  (a -> b) -> p a x -> p b x
    second :: (x -> y) -> p a x -> p a y

    laws:
    bimap id id = id
    - bimap using two identify function is identify
    bimap f h . bimap g i = bimap (f . g)(h . i)
    - bimap using two pair of functions is the same as bimap using composition of compositions of those functions
 */
object InstancesForForBuildInTypes {

  /* Haskell - instance for BiFunctor tuple

  instance Bifunctor (,) where
    bimap :: (a -> b) -> (x -> y) -> (a,x) -> (b,y)
    bimap f g (a,x) = (f, a g x)
   */
  val tuple2Bifunctor: Bifunctor[Tuple2] = new Bifunctor[Tuple2] {
    override def bimap[A, B, C, D](fab: (A, B))(f: A => C, g: B => D): (C, D) = (f(fab._1), g(fab._2))
  }

  /*
  Haskell - instance for BiFunctor for Either

  instance BiFunctor Either where
    bimap :: (a -> b) -> (x -> y) -> Either a x -> Either b y
    bimap f g (Left a) = Left (f a)
    bimap f g (Right x) = Right (g x)
   */
  val eitherBifunctor: Bifunctor[Either] = new Bifunctor[Either] {
    override def bimap[A, B, C, D](fab: Either[A, B])(f: A => C, g: B => D): Either[C, D] = fab match {
      case Left(v) => Left(f(v))
      case Right(v) => Right(g(v))
    }
  }

  import cats.data.Validated
  val validatedBifunctor: Bifunctor[Validated] = new Bifunctor[Validated] {
    override def bimap[A, B, C, D](fab: Validated[A, B])(f: A => C, g: B => D): Validated[C, D] = fab.map(g).leftMap(f)
  }

  val mapBifunctor: Bifunctor[Map] = new Bifunctor[Map] {
    override def bimap[A, B, C, D](fab: Map[A, B])(f: A => C, g: B => D): Map[C, D] = {
      fab.foldLeft(Map[C, D]()){ (soFar, entry) => soFar + ( f(entry._1) -> g(entry._2)) }
    }
  }
}
