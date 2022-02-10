package dev.insideyou
package invoicegenerator
package usecase2

import java.time.*

private object GateImpl:
  def make: Gate[LocalDate] =
    new:
      override def isWorkDay(date: LocalDate): Boolean =
        !Set(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)(date.getDayOfWeek)
