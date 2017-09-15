package com.moneytransfer.accounts

object TypeAliases {
  type AccountId = Int

  type ValidationError = String

  type Money = BigDecimal
  def Money(str: String) = BigDecimal(str)

}