package dev.insideyou
package invoicegenerator
package usecase2

import java.time.LocalDate

lazy val boundary: Boundary[LocalDate] =
  Boundary.make
