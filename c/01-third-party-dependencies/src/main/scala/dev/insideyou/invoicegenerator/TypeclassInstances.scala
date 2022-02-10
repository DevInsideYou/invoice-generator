package dev.insideyou
package invoicegenerator

import java.time.LocalDate

given PlusMonths[LocalDate] with
  extension (self: LocalDate)
    def plusMonths(months: Int): LocalDate =
      self.plusMonths(months).nn

given LastDayOfMonth with
  def apply(year: Int, month: Int): Int =
    val date = LocalDate.of(year, month, 1).nn

    date.getMonth.nn.length(date.isLeapYear)

given Date[LocalDate] with
  def make(
      year: Int,
      month: Int,
      day: Int,
    ): LocalDate = LocalDate.of(year, month, day).nn
