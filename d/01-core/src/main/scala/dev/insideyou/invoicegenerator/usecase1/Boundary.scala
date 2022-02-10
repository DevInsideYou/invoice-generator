package dev.insideyou
package invoicegenerator
package usecase1

import java.time.LocalDate

private trait Boundary:
  def generateInvoices(
      year: Int,
      firstInvoiceNumber: Int,
      rate: Option[Double],
    ): Vector[Invoice]

private object Boundary:
  def make(gate: Gate): Boundary =
    new:
      override def generateInvoices(
          year: Int,
          firstInvoiceNumber: Int,
          rate: Option[Double],
        ): Vector[Invoice] =
        (1 to 12)
          .foldLeft(Vector.empty[Invoice] -> firstInvoiceNumber) {
            case ((invoices, invoiceNumber), month) =>
              val newInvoices =
                invoices.concat(
                  Vector(
                    generateFirstInvoiceOfMonth(year, month, invoiceNumber, rate),
                    generateSecondInvoiceOfMonth(year, month, invoiceNumber + 1, rate),
                  )
                )

              val newInvoiceNumber =
                invoiceNumber + 2

              newInvoices -> newInvoiceNumber
          }
          ._1 // invoices

      private def generateFirstInvoiceOfMonth(
          year: Int,
          month: Int,
          invoiceNumber: Int,
          rate: Option[Double],
        ): Invoice =
        val days = makeDatesInOrder(year, month, from = 1, to = 15)
        val workHours = days.onlyWorkdays.inWorkHours
        val to, createdOn = days.last

        Invoice(
          number = invoiceNumber,
          createdOn = createdOn,
          dueOn = oneMonthAfter(createdOn),
          from = days.head,
          to = to,
          workHours = workHours,
          amount = workHours.inMoney(rate),
        )

      private def generateSecondInvoiceOfMonth(
          year: Int,
          month: Int,
          invoiceNumber: Int,
          rate: Option[Double],
        ): Invoice =
        val days = makeDatesInOrder(year, month, from = 16, to = lastDayOfMonth(year, month))
        val workHours = days.onlyWorkdays.inWorkHours
        val to = days.last
        val createdOn = LocalDate.of(year, month, 28).nn

        Invoice(
          number = invoiceNumber,
          createdOn = createdOn,
          dueOn = oneMonthAfter(createdOn),
          from = days.head,
          to = to,
          workHours = workHours,
          amount = workHours.inMoney(rate),
        )

      private def makeDatesInOrder(
          year: Int,
          month: Int,
          from: Int,
          to: Int,
        ): Vector[LocalDate] =
        (from to to).map(day => LocalDate.of(year, month, day).nn).toVector

      extension (self: Vector[LocalDate])
        private def onlyWorkdays: Vector[LocalDate] =
          self.filter(_.isWorkDay)

        private def inWorkHours: Int =
          self.length * gate.hoursInWorkDay

      extension (self: LocalDate)
        private def isWorkDay: Boolean =
          gate.isWorkDay(self)

      private def oneMonthAfter(createdOn: LocalDate): LocalDate =
        createdOn.plusMonths(1).nn

      extension (self: Int)
        private def inMoney(rate: Option[Double]): Option[Double] =
          rate.map(_ * self)

      private def lastDayOfMonth(year: Int, month: Int): Int =
        val date = LocalDate.of(year, month, 1).nn

        date.getMonth.nn.length(date.isLeapYear)
