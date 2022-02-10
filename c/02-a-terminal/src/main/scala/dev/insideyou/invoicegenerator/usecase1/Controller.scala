package dev.insideyou
package invoicegenerator
package usecase1

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private trait Controller:
  def run(
      year: Int,
      firstInvoiceNumber: Int,
      rate: Option[Double],
    ): Unit

private object Controller:
  def make(boundary: Boundary[InvoiceCaseClass]): Controller =
    new:
      override def run(
          year: Int,
          firstInvoiceNumber: Int,
          rate: Option[Double],
        ): Unit =
        val invoices =
          boundary.generateInvoices(year, firstInvoiceNumber, rate)

        println("─" * 30)

        invoices.map(_.rendered).foreach(println)

        if invoices.forall(_.amount.nonEmpty) then
          println("─" * 30)
          println(invoices.flatMap(_.amount).sum)

        println("─" * 30)

  extension (self: InvoiceCaseClass)
    private def rendered: String =
      import self.*

      val cd = s"${createdOn.rendered} - ${dueOn.rendered}"
      val ft = s"${from.rendered} - ${to.rendered} "
      val a = amount.fold("")(v => s" │ $v")

      s"$number │ ${cd.padTo(30, ' ')} │ ${ft.padTo(30, ' ')} │ $workHours$a"

  extension (self: LocalDate)
    private def rendered: String =
      DateTimeFormatter.ofPattern("MMMM d").nn.format(self).nn

final private case class InvoiceCaseClass(
    number: Int,
    createdOn: LocalDate,
    dueOn: LocalDate,
    from: LocalDate,
    to: LocalDate,
    workHours: Int,
    amount: Option[Double],
  )
