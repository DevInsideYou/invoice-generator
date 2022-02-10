package dev.insideyou
package invoicegenerator

trait LastDayOfMonth extends ((Int, Int) => Int)

trait PlusMonths[D]:
  extension (self: D) def plusMonths(months: Int): D

trait Filter[F[_]]:
  extension [A](self: F[A]) def filter(predicate: A => Boolean): F[A]

trait Length[F[_]]:
  extension [A](self: F[A]) def length: Int

trait First[F[_]]:
  extension [A](self: F[A]) def first: A

trait Last[F[_]]:
  extension [A](self: F[A]) def last: A

trait Map[F[_]]:
  extension [A](self: F[A]) def map[B](f: A => B): F[B]

// Special fold for scala.Range
trait FoldLeft[R]:
  extension (self: R) def foldLeft[B](seed: B)(f: (B, Int) => B): B

trait Concat[F[_]]:
  extension [A](self: F[A]) def concat(other: F[A]): F[A]

trait Get1[Tuple2[_, _]]:
  extension [A1, A2](self: Tuple2[A1, A2]) def _1: A1

trait Get2[Tuple2[_, _]]:
  extension [A1, A2](self: Tuple2[A1, A2]) def _2: A2

trait Tuple2[T2[_, _]]:
  def make[A1, A2](a1: A1, a2: A2): T2[A1, A2]

def Tuple2[T2[_, _]: Tuple2] = summon

trait Range[R]:
  def make(from: Int, to: Int): R

def Range[R: Range] = summon

trait Collection[C[_]]:
  def make[A](as: A*): C[A]

def Collection[C[_]: Collection] = summon

trait Date[D]:
  def make(
      year: Int,
      month: Int,
      day: Int,
    ): D

def Date[D: Date] = summon

trait DayOfWeek[Date, Day]:
  extension (self: Date) def dayOfWeek: Day

trait Saturday[Day]:
  def make: Day

def Saturday[Day: Saturday] = summon

trait Sunday[Day]:
  def make: Day

def Sunday[Day: Sunday] = summon

trait Equality[A]:
  extension (first: A) infix def sameAs(second: A): Boolean
