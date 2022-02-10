package dev.insideyou
package invoicegenerator
package usecase1

import java.time.LocalDate

lazy val controller: Controller =
  Controller.make(boundary)

private lazy val boundary: Boundary[InvoiceCaseClass] =
  Boundary.make[LocalDate, InvoiceCaseClass](
    gate = Gate.make(
      otherBoundaries = OtherBoundariesImpl.make(usecase2.boundary),
      config = ConfigImpl.make,
    )
  )

private given Invoice[InvoiceCaseClass, LocalDate] with
  def make(
      number: Int,
      createdOn: LocalDate,
      dueOn: LocalDate,
      from: LocalDate,
      to: LocalDate,
      workHours: Int,
      amount: Option[Double],
    ): InvoiceCaseClass = InvoiceCaseClass(number, createdOn, dueOn, from, to, workHours, amount)
