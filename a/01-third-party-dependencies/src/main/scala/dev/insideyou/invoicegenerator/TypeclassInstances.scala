package dev.insideyou
package invoicegenerator

import java.time.LocalDate

given PlusMonths[LocalDate] with
  extension (self: LocalDate)
    def plusMonths(months: Int): LocalDate =
      self.plusMonths(months).nn

given Filter[Vector] with
  extension [A](self: Vector[A])
    def filter(predicate: A => Boolean): Vector[A] =
      self.filter(predicate)

given Length[Vector] with
  extension [A](self: Vector[A])
    def length: Int =
      self.length

given First[Vector] with
  extension [A](self: Vector[A])
    def first: A =
      self(0)

given Last[Vector] with
  extension [A](self: Vector[A])
    def last: A =
      self.last

given Map[Option] with
  extension [A](self: Option[A])
    def map[B](f: A => B): Option[B] =
      self.map(f)

given Map[Vector] with
  extension [A](self: Vector[A])
    def map[B](f: A => B): Vector[B] =
      self.map(f)

given Concat[Vector] with
  extension [A](self: Vector[A])
    def concat(other: Vector[A]): Vector[A] =
      self.concat(other)

given Get1[scala.Tuple2] with
  extension [A1, A2](self: scala.Tuple2[A1, A2])
    def _1: A1 =
      self._1

given Get2[scala.Tuple2] with
  extension [A1, A2](self: scala.Tuple2[A1, A2])
    def _2: A2 =
      self._2

given LastDayOfMonth with
  def apply(year: Int, month: Int): Int =
    val date = LocalDate.of(year, month, 1).nn

    date.getMonth.nn.length(date.isLeapYear)

given Tuple2[scala.Tuple2] with
  def make[A1, A2](a1: A1, a2: A2): scala.Tuple2[A1, A2] =
    (a1, a2)

given Range[scala.Range] with
  def make(from: Int, to: Int): scala.Range =
    from to to

given FoldLeft[scala.Range] with
  extension (self: scala.Range)
    def foldLeft[B](seed: B)(f: (B, Int) => B): B =
      self.foldLeft(seed)(f)

given Collection[Vector] with
  def make[A](as: A*): Vector[A] =
    Vector(as*)

given Date[LocalDate] with
  def make(
      year: Int,
      month: Int,
      day: Int,
    ): LocalDate = LocalDate.of(year, month, day).nn

given Conversion[scala.Range, Vector[Int]] = _.toVector

given DayOfWeek[LocalDate, java.time.DayOfWeek] with
  extension (self: LocalDate)
    def dayOfWeek: java.time.DayOfWeek =
      self.getDayOfWeek.nn

given Saturday[java.time.DayOfWeek] with
  def make: java.time.DayOfWeek =
    java.time.DayOfWeek.SATURDAY

given Sunday[java.time.DayOfWeek] with
  def make: java.time.DayOfWeek =
    java.time.DayOfWeek.SUNDAY

given Equality[java.time.DayOfWeek] with
  extension (first: java.time.DayOfWeek)
    infix def sameAs(second: java.time.DayOfWeek): Boolean =
      first == second
