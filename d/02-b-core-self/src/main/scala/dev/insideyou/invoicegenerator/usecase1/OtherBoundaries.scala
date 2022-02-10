package dev.insideyou
package invoicegenerator
package usecase1

import java.time.LocalDate

private object OtherBoundariesImpl:
  def make(usecase2Boundary: usecase2.Boundary): OtherBoundaries =
    new:
      export usecase2Boundary.*
