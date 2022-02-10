package dev.insideyou
package invoicegenerator
package usecase2

trait Boundary[D]:
  def isWorkDay(date: D): Boolean

private object Boundary:
  def make[D](gate: Gate[D]): Boundary[D] =
    new:
      export gate.*
