package dev.insideyou
package invoicegenerator
package usecase1

import java.time.LocalDate

final class BoundarySuite extends TestSuite:
  import BoundarySuite.*
  import BoundarySuite.given

  test("no work days") {
    val boundary =
      make(new:
        override def hoursInWorkDay: Int = 8
        override def isWorkDay(date: LocalDate): Boolean = false
      )

    val invoices =
      boundary.generateInvoices(
        year = 2022,
        firstInvoiceNumber = 23,
        rate = None,
      )

    invoices.map(_.workHours).forall(_ == 0) `shouldBe` true
  }

  test("all work days") {
    object input:
      val hoursInWorkDay = 8

    val boundary =
      make(new:
        override def hoursInWorkDay: Int = input.hoursInWorkDay
        override def isWorkDay(date: LocalDate): Boolean = true
      )

    val invoices =
      boundary.generateInvoices(
        year = 2022,
        firstInvoiceNumber = 23,
        rate = None,
      )

    invoices.map(_.workHours).sum `shouldBe` (365 * input.hoursInWorkDay)
  }

  test("all work days in a leap year") {
    object input:
      val hoursInWorkDay = 8

    val boundary =
      make(new:
        override def hoursInWorkDay: Int = input.hoursInWorkDay
        override def isWorkDay(date: LocalDate): Boolean = true
      )

    val invoices =
      boundary.generateInvoices(
        year = 2024,
        firstInvoiceNumber = 23,
        rate = None,
      )

    invoices.map(_.workHours).sum `shouldBe` (366 * input.hoursInWorkDay)
  }

  private def make(gate: FakeGate): Boundary =
    Boundary.make(gate)

private object BoundarySuite:
  private class FakeGate extends Gate:
    override def hoursInWorkDay: Int = ???
    override def isWorkDay(date: LocalDate): Boolean = ???
