package com.moneytransfer.accounts

import com.moneytransfer.accounts.TypeAliases.AccountId

object ValidationMessages {
  def AccountNotFound(accountId: AccountId) = "No account exists with id: " + accountId
  def AccountsMustDiffer = "The source and target account of transfer must be different accounts"
  def InsufficientBalance = "Insufficient balance"
  def AmountMustBePositive = "Amount must be positive"

}