package dev.insideyou
package invoicegenerator
package usecase1

import java.time.LocalDate

private object OtherBoundariesImpl:
  def make[D](usecase2Boundary: usecase2.Boundary[D]): OtherBoundaries[D] =
    new:
      export usecase2Boundary.*
