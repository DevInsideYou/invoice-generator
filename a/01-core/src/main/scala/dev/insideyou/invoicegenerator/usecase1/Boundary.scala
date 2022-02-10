package dev.insideyou
package invoicegenerator
package usecase1

private trait Boundary[MultipleInvoices[_], Maybe[_], Invoice]:
  def generateInvoices(
      year: Int,
      firstInvoiceNumber: Int,
      rate: Maybe[Double],
    ): MultipleInvoices[Invoice]

private object Boundary:
  def make[
      D: Date: PlusMonths,
      I[_[_]],
      Maybe[_]: Map,
      MultipleDates[_]: Map: Filter: Length: First: Last,
      MultipleInvoices[_]: Collection: Concat,
      R: Range: FoldLeft,
      T2[_, _]: Tuple2: Get1: Get2,
    ](
      gate: Gate[D]
    )(using
      Invoice[I, D, Maybe]
    )(using
      convert: Conversion[R, MultipleDates[Int]],
      lastDayOfMonth: LastDayOfMonth,
    ): Boundary[MultipleInvoices, Maybe, I[Maybe]] =
    new:
      override def generateInvoices(
          year: Int,
          firstInvoiceNumber: Int,
          rate: Maybe[Double],
        ): MultipleInvoices[I[Maybe]] =
        Range
          .make(1, 12)
          .foldLeft(Tuple2.make(Collection.make[I[Maybe]](), firstInvoiceNumber)) {
            (tuple, month) =>
              val invoices = tuple._1
              val invoiceNumber = tuple._2

              val newInvoices =
                invoices.concat(
                  Collection.make(
                    generateFirstInvoiceOfMonth(year, month, invoiceNumber, rate),
                    generateSecondInvoiceOfMonth(year, month, invoiceNumber + 1, rate),
                  )
                )

              val newInvoiceNumber =
                invoiceNumber + 2

              Tuple2.make(newInvoices, newInvoiceNumber)
          }
          ._1 // invoices

      private def generateFirstInvoiceOfMonth(
          year: Int,
          month: Int,
          invoiceNumber: Int,
          rate: Maybe[Double],
        ): I[Maybe] =
        val days = makeDatesInOrder(year, month, from = 1, to = 15)
        val workHours = days.onlyWorkdays.inWorkHours
        val to, createdOn = days.last

        Invoice.make(
          number = invoiceNumber,
          createdOn = createdOn,
          dueOn = oneMonthAfter(createdOn),
          from = days.first,
          to = to,
          workHours = workHours,
          amount = workHours.inMoney(rate),
        )

      private def generateSecondInvoiceOfMonth(
          year: Int,
          month: Int,
          invoiceNumber: Int,
          rate: Maybe[Double],
        ): I[Maybe] =
        val days = makeDatesInOrder(year, month, from = 16, to = lastDayOfMonth(year, month))
        val workHours = days.onlyWorkdays.inWorkHours
        val to = days.last
        val createdOn = Date.make(year, month, 28)

        Invoice.make(
          number = invoiceNumber,
          createdOn = createdOn,
          dueOn = oneMonthAfter(createdOn),
          from = days.first,
          to = to,
          workHours = workHours,
          amount = workHours.inMoney(rate),
        )

      private def makeDatesInOrder(
          year: Int,
          month: Int,
          from: Int,
          to: Int,
        ): MultipleDates[D] =
        convert(Range.make(from, to)).map(day => Date.make(year, month, day))

      extension (self: MultipleDates[D])
        private def onlyWorkdays: MultipleDates[D] =
          self.filter(_.isWorkDay)

        private def inWorkHours: Int =
          self.length * gate.hoursInWorkDay

      extension (self: D)
        private def isWorkDay: Boolean =
          gate.isWorkDay(self)

      private def oneMonthAfter(createdOn: D): D =
        createdOn.plusMonths(1)

      extension (self: Int)
        private def inMoney(rate: Maybe[Double]): Maybe[Double] =
          rate.map(_ * self)

trait Invoice[I[_[_]], D, M[_]]:
  def make(
      number: Int,
      createdOn: D,
      dueOn: D,
      from: D,
      to: D,
      workHours: Int,
      amount: M[Double],
    ): I[M]

def Invoice[I[_[_]], D, M[_]](using Invoice[I, D, M]) = summon
