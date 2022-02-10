package dev.insideyou
package invoicegenerator
package usecase1

private trait Boundary[Invoice]:
  def generateInvoices(
      year: Int,
      firstInvoiceNumber: Int,
      rate: Option[Double],
    ): Vector[Invoice]

private object Boundary:
  def make[D: Date: PlusMonths, I](
      gate: Gate[D]
    )(using
      Invoice[I, D]
    )(using
      lastDayOfMonth: LastDayOfMonth
    ): Boundary[I] =
    new:
      override def generateInvoices(
          year: Int,
          firstInvoiceNumber: Int,
          rate: Option[Double],
        ): Vector[I] =
        (1 to 12)
          .foldLeft(Vector.empty[I] -> firstInvoiceNumber) {
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
        ): I =
        val days = makeDatesInOrder(year, month, from = 1, to = 15)
        val workHours = days.onlyWorkdays.inWorkHours
        val to, createdOn = days.last

        Invoice.make(
          number = invoiceNumber,
          createdOn = createdOn,
          dueOn = oneMonthAfter(createdOn),
          from = days.head, // here
          to = to,
          workHours = workHours,
          amount = workHours.inMoney(rate),
        )

      private def generateSecondInvoiceOfMonth(
          year: Int,
          month: Int,
          invoiceNumber: Int,
          rate: Option[Double],
        ): I =
        val days = makeDatesInOrder(year, month, from = 16, to = lastDayOfMonth(year, month))
        val workHours = days.onlyWorkdays.inWorkHours
        val to = days.last
        val createdOn = Date.make(year, month, 28)

        Invoice.make(
          number = invoiceNumber,
          createdOn = createdOn,
          dueOn = oneMonthAfter(createdOn),
          from = days.head, // here
          to = to,
          workHours = workHours,
          amount = workHours.inMoney(rate),
        )

      private def makeDatesInOrder(
          year: Int,
          month: Int,
          from: Int,
          to: Int,
        ): Vector[D] =
        (from to to).map(day => Date.make(year, month, day)).toVector

      extension (self: Vector[D])
        private def onlyWorkdays: Vector[D] =
          self.filter(_.isWorkDay)

        private def inWorkHours: Int =
          self.length * gate.hoursInWorkDay

      extension (self: D)
        private def isWorkDay: Boolean =
          gate.isWorkDay(self)

      private def oneMonthAfter(createdOn: D): D =
        createdOn.plusMonths(1)

      extension (self: Int)
        private def inMoney(rate: Option[Double]): Option[Double] =
          rate.map(_ * self)

trait Invoice[I, D]:
  def make(
      number: Int,
      createdOn: D,
      dueOn: D,
      from: D,
      to: D,
      workHours: Int,
      amount: Option[Double],
    ): I

def Invoice[I, D](using Invoice[I, D]) = summon
