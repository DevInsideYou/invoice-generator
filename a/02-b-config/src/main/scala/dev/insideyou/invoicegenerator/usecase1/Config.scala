package dev.insideyou
package invoicegenerator
package usecase1

private object ConfigImpl:
  def make: Config =
    new:
      override def hoursInWorkDay: Int =
        8
