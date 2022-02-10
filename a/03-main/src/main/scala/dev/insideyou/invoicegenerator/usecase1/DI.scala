package dev.insideyou
package invoicegenerator
package usecase1

import java.time.LocalDate

lazy val controller: Controller =
  Controller.make(boundary)

private lazy val boundary: Boundary[Vector, Option, I[Option]] =
  Boundary.make[LocalDate, I, Option, Vector, Vector, scala.Range, scala.Tuple2](
    gate = Gate.make(
      otherBoundaries = OtherBoundariesImpl.make(usecase2.boundary),
      config = ConfigImpl.make,
    )
  )

private type I[_[_]] = InvoiceCaseClass

private given Invoice[I, LocalDate, Option] with
  def make(
      number: Int,
      createdOn: LocalDate,
      dueOn: LocalDate,
      from: LocalDate,
      to: LocalDate,
      workHours: Int,
      amount: Option[Double],
    ): I[Option] = InvoiceCaseClass(number, createdOn, dueOn, from, to, workHours, amount)
