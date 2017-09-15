package com.moneytransfer.accounts

import com.moneytransfer.accounts.TypeAliases.{AccountId, Money, ValidationError}

import scalaz.Validation

trait BankService
{
  def GetAccountInformation(accountId: AccountId): Validation[ValidationError, AccountStatement]

  def OpenAccount(): Validation[ValidationError, Open]

  def IntrabankTransfer(fromAccountId: AccountId, toAccountId: AccountId, amount: Money): Validation[ValidationError, Transfer]

  def Deposit(accountId: AccountId, amount: Money): Validation[ValidationError, DepositCash]
}
