package com.moneytransfer.accounts

import com.moneytransfer.accounts.TypeAliases._

trait Transaction {
  def accountId: AccountId
}

case class Transfer(fromAccountId: AccountId, accountId: AccountId, amount: Money) extends Transaction
case class Open(accountId: AccountId) extends Transaction
case class DepositCash(accountId: AccountId, amount: Money) extends Transaction
