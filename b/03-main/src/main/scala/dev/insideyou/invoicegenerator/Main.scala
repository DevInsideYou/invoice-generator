package dev.insideyou
package invoicegenerator

@main def Main: Unit =
  usecase1.controller.run(year = 2022, firstInvoiceNumber = 23, rate = None)
