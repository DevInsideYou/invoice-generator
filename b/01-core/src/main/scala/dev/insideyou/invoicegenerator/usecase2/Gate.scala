package dev.insideyou
package invoicegenerator
package usecase2

private trait Gate[D]:
  def isWorkDay(date: D): Boolean
