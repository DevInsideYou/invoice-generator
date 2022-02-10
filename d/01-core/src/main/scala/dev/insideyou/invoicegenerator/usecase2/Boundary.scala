package dev.insideyou
package invoicegenerator
package usecase2

import java.time.*

trait Boundary:
  def isWorkDay(date: LocalDate): Boolean

private object Boundary:
  lazy val make: Boundary =
    new:
      override def isWorkDay(date: LocalDate): Boolean =
        !(Set(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))(date.getDayOfWeek)
