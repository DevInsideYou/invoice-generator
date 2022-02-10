package dev.insideyou
package invoicegenerator

trait LastDayOfMonth extends ((Int, Int) => Int)

trait PlusMonths[Date]:
  extension (self: Date) def plusMonths(months: Int): Date

trait Date[D]:
  def make(
      year: Int,
      month: Int,
      day: Int,
    ): D

def Date[D: Date] = summon
