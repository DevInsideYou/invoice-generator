package dev.insideyou
package invoicegenerator
package usecase2

trait Boundary[D]:
  def isWorkDay(date: D): Boolean

private object Boundary:
  def make[Date, Day: Equality: Saturday: Sunday](using DayOfWeek[Date, Day]): Boundary[Date] =
    new:
      override def isWorkDay(date: Date): Boolean =
        !(date.dayOfWeek.sameAs(Saturday.make) || date.dayOfWeek.sameAs(Sunday.make))
