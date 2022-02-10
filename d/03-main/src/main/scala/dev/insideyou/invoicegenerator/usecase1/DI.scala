package dev.insideyou
package invoicegenerator
package usecase1

lazy val controller: Controller =
  Controller.make(boundary)

private lazy val boundary: Boundary =
  Boundary.make(
    gate = Gate.make(
      otherBoundaries = OtherBoundariesImpl.make(usecase2.boundary),
      config = ConfigImpl.make,
    )
  )
