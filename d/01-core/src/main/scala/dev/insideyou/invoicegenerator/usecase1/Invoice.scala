package dev.insideyou.invoicegenerator.usecase1

import java.time.LocalDate

final private case class Invoice(
    number: Int,
    createdOn: LocalDate,
    dueOn: LocalDate,
    from: LocalDate,
    to: LocalDate,
    workHours: Int,
    amount: Option[Double],
  )
