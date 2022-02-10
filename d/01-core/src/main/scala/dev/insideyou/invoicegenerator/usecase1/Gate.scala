package dev.insideyou
package invoicegenerator
package usecase1

import java.time.LocalDate

private trait Gate extends Config, OtherBoundaries

private object Gate:
  def make(config: Config, otherBoundaries: OtherBoundaries): Gate =
    new:
      export config.*, otherBoundaries.*

private trait Config:
  def hoursInWorkDay: Int

private trait OtherBoundaries:
  def isWorkDay(date: LocalDate): Boolean
