package dev.insideyou
package invoicegenerator
package usecase1

private trait Gate[D] extends Config, OtherBoundaries[D]

private object Gate:
  def make[D](config: Config, otherBoundaries: OtherBoundaries[D]): Gate[D] =
    new:
      export config.*, otherBoundaries.*

private trait Config:
  def hoursInWorkDay: Int

private trait OtherBoundaries[D]:
  def isWorkDay(date: D): Boolean
